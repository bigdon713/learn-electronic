const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated users cannot read or write any user collections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const past = new Date(Date.now() - 1000);

  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("routines").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("routineLogs").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("lessonProgress").get());

  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("routines").doc("r1").set({
      userId: ALICE_UID,
      title: "Morning Math",
      category: "STUDY",
      subjectId: "math",
      startMinutes: 540,
      durationMinutes: 45,
      daysOfWeek: [1, 2, 3, 4, 5],
      note: "",
      createdAt: past,
      updatedAt: past,
    })
  );
});

test("Authenticated owner can create, list, update, and delete RoutineItem", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = new Date(Date.now() - 5000);

  const routineRef = aliceDb.collection("users").doc(ALICE_UID).collection("routines").doc("routine_1");

  await assertSucceeds(
    routineRef.set({
      userId: ALICE_UID,
      title: "Morning Math Puzzles",
      category: "STUDY",
      subjectId: "math",
      startMinutes: 540,
      durationMinutes: 30,
      daysOfWeek: [1, 2, 3, 4, 5],
      note: "Do 10 puzzle cards",
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("routines").get()
  );

  await assertSucceeds(
    routineRef.update({
      title: "Morning Math & Logic",
      durationMinutes: 45,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reject immutable field mutation
  await assertFails(
    routineRef.update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Reject ghost field
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("routines").doc("routine_2").set({
      id: "routine_2",
      userId: ALICE_UID,
      title: "Bad Doc",
      category: "STUDY",
      subjectId: "math",
      startMinutes: 600,
      durationMinutes: 30,
      daysOfWeek: [1],
      note: "",
      createdAt: created,
      updatedAt: created,
    })
  );

  // Reject future timestamp
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("routines").doc("routine_future").set({
      userId: ALICE_UID,
      title: "Future Doc",
      category: "PLAY",
      subjectId: "",
      startMinutes: 600,
      durationMinutes: 30,
      daysOfWeek: [1],
      note: "",
      createdAt: new Date(Date.now() + 60000),
      updatedAt: created,
    })
  );

  await assertSucceeds(routineRef.delete());
});

test("Authenticated owner can create and update RoutineLog and LessonProgress", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = new Date(Date.now() - 5000);

  const logRef = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("routineLogs")
    .doc("2026-11-18_routine_1");

  await assertSucceeds(
    logRef.set({
      userId: ALICE_UID,
      routineId: "routine_1",
      dateKey: "2026-11-18",
      completed: true,
      starsEarned: 5,
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    logRef.update({
      completed: false,
      starsEarned: 0,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  const progressRef = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("lessonProgress")
    .doc("math_1");

  await assertSucceeds(
    progressRef.set({
      userId: ALICE_UID,
      subjectId: "math",
      lessonId: "math_1",
      completed: true,
      bestScore: 3,
      totalQuestions: 3,
      starsEarned: 15,
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    progressRef.update({
      bestScore: 3,
      starsEarned: 15,
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});

test("Cross-user isolation: Alice cannot read or write Bob's routines, logs, or lessonProgress", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    const db = context.firestore();
    const created = new Date(Date.now() - 10000);
    await db.collection("users").doc(BOB_UID).collection("routines").doc("bob_r1").set({
      userId: BOB_UID,
      title: "Bob Reading",
      category: "READING",
      subjectId: "reading",
      startMinutes: 600,
      durationMinutes: 30,
      daysOfWeek: [1, 2],
      note: "",
      createdAt: created,
      updatedAt: created,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("routines").get());
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("routines").doc("bob_r1").get());
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("routines").doc("bob_r1").update({
      title: "Hijacked",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("lessonProgress").doc("math_1").set({
      userId: ALICE_UID,
      subjectId: "math",
      lessonId: "math_1",
      completed: true,
      bestScore: 3,
      totalQuestions: 3,
      starsEarned: 15,
      createdAt: new Date(Date.now() - 1000),
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});
