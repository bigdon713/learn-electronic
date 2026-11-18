package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Science
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.CobaltSoft
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CoralSoft
import com.example.ui.theme.LavenderPlum
import com.example.ui.theme.LavenderSoft
import com.example.ui.theme.MeadowEmerald
import com.example.ui.theme.MeadowSoft
import com.example.ui.theme.SkyCerulean
import com.example.ui.theme.SkySoft
import com.example.ui.theme.SunburstGold
import com.example.ui.theme.SunburstSoft

data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

data class Lesson(
    val id: String,
    val subjectId: String,
    val title: String,
    val durationMinutes: Int,
    val ageBadge: String,
    val storyIntro: String,
    val keyPoints: List<String>,
    val funFact: String,
    val tryItActivity: String,
    val quiz: List<QuizQuestion>,
)

data class Subject(
    val id: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color,
    val containerColor: Color,
    val icon: ImageVector,
    val lessons: List<Lesson>,
)

data class StarterRoutineTemplate(
    val title: String,
    val category: RoutineCategory,
    val subjectId: String,
    val startMinutes: Long,
    val durationMinutes: Long,
    val note: String,
)

data class ExplorerBadge(
    val id: String,
    val title: String,
    val description: String,
    val requirementLabel: String,
)

object CurriculumCatalog {

    val holidayTips: List<String> = listOf(
        "Break big study blocks into 25-minute adventures with a 10-minute stretch break!",
        "Try teaching one fun fact from today's lesson to someone at dinner.",
        "Keep a glass of water by your study spot to help your brain stay sharp.",
        "Balance screen learning with outdoor play and hands-on crafts every afternoon.",
        "Check off your routine items right after finishing them to watch your star streak grow!",
    )

    val starterHolidayRoutines: List<StarterRoutineTemplate> = listOf(
        StarterRoutineTemplate(
            title = "Morning Stretch & Breakfast",
            category = RoutineCategory.REST,
            subjectId = "",
            startMinutes = 510L, // 8:30 AM
            durationMinutes = 45L,
            note = "Drink water, eat a healthy breakfast, and get ready for the day.",
        ),
        StarterRoutineTemplate(
            title = "Math Magic Explorers",
            category = RoutineCategory.STUDY,
            subjectId = "math",
            startMinutes = 570L, // 9:30 AM
            durationMinutes = 30L,
            note = "Complete one Math Magic lesson and quiz.",
        ),
        StarterRoutineTemplate(
            title = "Snack & Free Play",
            category = RoutineCategory.PLAY,
            subjectId = "",
            startMinutes = 615L, // 10:15 AM
            durationMinutes = 45L,
            note = "Rest your eyes and move around!",
        ),
        StarterRoutineTemplate(
            title = "Reading & Story Adventure",
            category = RoutineCategory.READING,
            subjectId = "reading",
            startMinutes = 675L, // 11:15 AM
            durationMinutes = 30L,
            note = "Read a chapter or finish a Reading & Stories quest.",
        ),
        StarterRoutineTemplate(
            title = "Lunch & Quiet Time",
            category = RoutineCategory.REST,
            subjectId = "",
            startMinutes = 750L, // 12:30 PM
            durationMinutes = 60L,
            note = "Enjoy lunch and relax.",
        ),
        StarterRoutineTemplate(
            title = "Science or Art Discovery",
            category = RoutineCategory.CREATIVE,
            subjectId = "science",
            startMinutes = 870L, // 2:30 PM
            durationMinutes = 45L,
            note = "Try a hands-on Science Lab or Creative Arts activity.",
        ),
        StarterRoutineTemplate(
            title = "Tidy Room & Outdoor Play",
            category = RoutineCategory.CHORES,
            subjectId = "",
            startMinutes = 960L, // 4:00 PM
            durationMinutes = 60L,
            note = "Put toys away first, then head outside for fresh air!",
        ),
    )

    val subjects: List<Subject> = listOf(
        Subject(
            id = "math",
            title = "Math Magic",
            subtitle = "Puzzles, fractions & number tricks",
            accentColor = SunburstGold,
            containerColor = SunburstSoft,
            icon = Icons.Rounded.Calculate,
            lessons = listOf(
                Lesson(
                    id = "math_1",
                    subjectId = "math",
                    title = "Pizza Party Fractions",
                    durationMinutes = 10,
                    ageBadge = "Ages 7–11",
                    storyIntro = "Imagine sharing a giant warm pizza equally with your friends! Fractions help us split whole things into fair, equal slices.",
                    keyPoints = listOf(
                        "The bottom number (denominator) tells how many equal slices make one whole pizza.",
                        "The top number (numerator) tells how many slices you have on your plate.",
                        "1/2 is the same amount of pizza as 2/4 or 4/8 — these are called equivalent fractions!",
                    ),
                    funFact = "The line between the top and bottom numbers in a fraction has a cool name: it's called a vinculum!",
                    tryItActivity = "Fold a piece of paper in half, then in half again. Unfold it and color 2 out of the 4 boxes to see 2/4 = 1/2!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "A pie is cut into 8 equal slices. Maya eats 3 slices. What fraction did she eat?",
                            options = listOf("3/8", "8/3", "3/5", "1/3"),
                            correctIndex = 0,
                            explanation = "3 slices out of 8 total equal slices is written as 3/8.",
                        ),
                        QuizQuestion(
                            prompt = "Which fraction is equal to one half (1/2)?",
                            options = listOf("2/3", "3/6", "1/4", "4/5"),
                            correctIndex = 1,
                            explanation = "3 is half of 6, so 3/6 equals 1/2!",
                        ),
                        QuizQuestion(
                            prompt = "Which slice is bigger: 1/2 of a cake or 1/4 of the same cake?",
                            options = listOf("1/4", "They are the same", "1/2", "Cannot tell"),
                            correctIndex = 2,
                            explanation = "Cutting a cake into 2 pieces makes bigger slices than cutting it into 4 pieces.",
                        ),
                    ),
                ),
                Lesson(
                    id = "math_2",
                    subjectId = "math",
                    title = "Multiplication Shortcuts",
                    durationMinutes = 12,
                    ageBadge = "Ages 7–11",
                    storyIntro = "Multiplication is super-fast repeated addition! With a few smart patterns, you can solve tricky times tables in seconds.",
                    keyPoints = listOf(
                        "Multiplying by 5 always ends in 0 or 5 (like 5, 10, 15, 20).",
                        "Multiplying by 9? Hold up 10 fingers, tuck the finger for the number you multiply by, and read tens + ones!",
                        "Order doesn't change the answer: 4 x 7 is the exact same as 7 x 4.",
                    ),
                    funFact = "Every answer in the 9 times table (up to 9 x 10) has digits that add up to 9! Try 9 x 4 = 36 -> 3 + 6 = 9.",
                    tryItActivity = "Use coins or dried beans to build 3 rows of 6, then turn it sideways to see 6 rows of 3!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What is 6 x 5?",
                            options = listOf("25", "30", "35", "36"),
                            correctIndex = 1,
                            explanation = "Counting by 5 six times gives 5, 10, 15, 20, 25, 30.",
                        ),
                        QuizQuestion(
                            prompt = "Using the 9s digit trick, what is 9 x 7?",
                            options = listOf("56", "63", "72", "54"),
                            correctIndex = 1,
                            explanation = "9 x 7 = 63, and notice 6 + 3 = 9!",
                        ),
                        QuizQuestion(
                            prompt = "If 8 x 4 = 32, what is 4 x 8?",
                            options = listOf("24", "36", "32", "28"),
                            correctIndex = 2,
                            explanation = "Because multiplication can swap order, 4 x 8 also equals 32.",
                        ),
                    ),
                ),
                Lesson(
                    id = "math_3",
                    subjectId = "math",
                    title = "Shape Detective",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–10",
                    storyIntro = "Look around your room! Books, soccer balls, windows, and pizza slices are all geometry shapes hiding in plain sight.",
                    keyPoints = listOf(
                        "A polygon is a flat closed shape with straight sides, like triangles (3 sides) and hexagons (6 sides).",
                        "Perimeter is the distance all the way around the outside edge of a shape.",
                        "3D shapes like cubes and spheres have depth — you can hold them in your hand!",
                    ),
                    funFact = "Bees build honeycomb cells as hexagons because hexagons pack together with zero wasted space!",
                    tryItActivity = "Go on a 2-minute Shape Hunt at home and find 1 rectangle, 1 circle, and 1 cylinder.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "How many sides does a hexagon have?",
                            options = listOf("5", "6", "7", "8"),
                            correctIndex = 1,
                            explanation = "Hexa means six — a hexagon has 6 straight sides.",
                        ),
                        QuizQuestion(
                            prompt = "A square garden has sides that are 4 meters long. What is its perimeter?",
                            options = listOf("8 meters", "12 meters", "16 meters", "20 meters"),
                            correctIndex = 2,
                            explanation = "4 + 4 + 4 + 4 = 16 meters all the way around.",
                        ),
                        QuizQuestion(
                            prompt = "Which of these is a 3D shape?",
                            options = listOf("Triangle", "Cube", "Square", "Oval"),
                            correctIndex = 1,
                            explanation = "A cube has height, width, and depth like a dice!",
                        ),
                    ),
                ),
            ),
        ),
        Subject(
            id = "science",
            title = "Science Lab",
            subtitle = "Space, plants & kitchen wonders",
            accentColor = MeadowEmerald,
            containerColor = MeadowSoft,
            icon = Icons.Rounded.Science,
            lessons = listOf(
                Lesson(
                    id = "science_1",
                    subjectId = "science",
                    title = "Tour of the Solar System",
                    durationMinutes = 12,
                    ageBadge = "Ages 7–12",
                    storyIntro = "Put on your astronaut helmet! Eight amazing planets orbit around our Sun, each with its own wild weather and moons.",
                    keyPoints = listOf(
                        "The 4 inner planets (Mercury, Venus, Earth, Mars) are rocky worlds.",
                        "The 4 outer planets (Jupiter, Saturn, Uranus, Neptune) are giant balls of gas and ice.",
                        "Earth sits in the 'Goldilocks Zone' — not too hot and not too cold for liquid water.",
                    ),
                    funFact = "A day on Venus is longer than its entire year because Venus spins super slowly!",
                    tryItActivity = "Line up fruits from small (a blueberry for Mercury) to huge (a watermelon for Jupiter) to model planet sizes!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "Which planet is famous for its bright icy rings?",
                            options = listOf("Mars", "Saturn", "Mercury", "Venus"),
                            correctIndex = 1,
                            explanation = "Saturn has thousands of shimmering rings made of ice and rock.",
                        ),
                        QuizQuestion(
                            prompt = "Which is the largest planet in our Solar System?",
                            options = listOf("Earth", "Neptune", "Jupiter", "Mars"),
                            correctIndex = 2,
                            explanation = "Jupiter is a gas giant bigger than all other planets combined!",
                        ),
                        QuizQuestion(
                            prompt = "What do the inner four planets have in common?",
                            options = listOf("They are made of solid rock", "They all have rings", "They are made of ice", "They glow like stars"),
                            correctIndex = 0,
                            explanation = "Mercury, Venus, Earth, and Mars are rocky terrestrial planets.",
                        ),
                    ),
                ),
                Lesson(
                    id = "science_2",
                    subjectId = "science",
                    title = "How Plants Make Food",
                    durationMinutes = 10,
                    ageBadge = "Ages 7–11",
                    storyIntro = "Plants don't visit the grocery store! Instead, their green leaves act like tiny solar-powered kitchens.",
                    keyPoints = listOf(
                        "Leaves absorb sunlight using a green pigment called chlorophyll.",
                        "Roots drink up water from the soil, while leaves take in carbon dioxide from the air.",
                        "Using sunlight, water, and air, plants make sugar for energy and release fresh oxygen for us!",
                    ),
                    funFact = "This amazing process is called photosynthesis — 'photo' means light, and 'synthesis' means putting together!",
                    tryItActivity = "Put a leafy celery stalk in water with a drop of food coloring and watch the colored water climb up!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What gas do plants release that humans and animals breathe?",
                            options = listOf("Oxygen", "Helium", "Nitrogen", "Smoke"),
                            correctIndex = 0,
                            explanation = "Plants release fresh oxygen into the air during photosynthesis.",
                        ),
                        QuizQuestion(
                            prompt = "Which part of a plant drinks up water from the soil?",
                            options = listOf("Petals", "Roots", "Seeds", "Bark"),
                            correctIndex = 1,
                            explanation = "Roots reach into the soil to absorb water and minerals.",
                        ),
                        QuizQuestion(
                            prompt = "What gives leaves their green color and catches sunlight?",
                            options = listOf("Chlorophyll", "Gravity", "Pollen", "Calcium"),
                            correctIndex = 0,
                            explanation = "Chlorophyll makes leaves green and captures energy from sunlight.",
                        ),
                    ),
                ),
                Lesson(
                    id = "science_3",
                    subjectId = "science",
                    title = "Solid, Liquid, Gas!",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–10",
                    storyIntro = "Everything you can touch, drink, or breathe is matter — and matter loves to change costumes when heated or cooled!",
                    keyPoints = listOf(
                        "Solids keep their own shape because their tiny particles are packed tightly together.",
                        "Liquids flow and take the shape of whatever cup or bowl holds them.",
                        "Gases spread out to fill all the space around them, like air inside a balloon.",
                    ),
                    funFact = "Water is the only everyday substance you regularly see as a solid (ice), liquid (water), and gas (steam)!",
                    tryItActivity = "Place an ice cube on a saucer in the sun and watch it melt from solid to liquid.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "Which state of matter keeps its own firm shape?",
                            options = listOf("Liquid", "Gas", "Solid", "Steam"),
                            correctIndex = 2,
                            explanation = "Solids like wooden blocks keep their shape unless broken or melted.",
                        ),
                        QuizQuestion(
                            prompt = "What happens when liquid water gets cold enough to freeze?",
                            options = listOf("It becomes a gas", "It turns into solid ice", "It disappears", "It turns into sand"),
                            correctIndex = 1,
                            explanation = "Freezing turns liquid water into solid ice at 0°C (32°F).",
                        ),
                        QuizQuestion(
                            prompt = "Air inside a soccer ball is an example of a:",
                            options = listOf("Solid", "Liquid", "Gas", "Metal"),
                            correctIndex = 2,
                            explanation = "Air is a mixture of gases that fills the whole inside of the ball.",
                        ),
                    ),
                ),
            ),
        ),
        Subject(
            id = "reading",
            title = "Reading & Stories",
            subtitle = "Word builders, tales & grammar",
            accentColor = CoralBerry,
            containerColor = CoralSoft,
            icon = Icons.Rounded.AutoStories,
            lessons = listOf(
                Lesson(
                    id = "reading_1",
                    subjectId = "reading",
                    title = "Story Mountain Builders",
                    durationMinutes = 12,
                    ageBadge = "Ages 7–11",
                    storyIntro = "Every great adventure story climbs a mountain — starting at the base, reaching an exciting peak, and landing safely home!",
                    keyPoints = listOf(
                        "Beginning: Meet the main character and discover the setting (where and when).",
                        "Climax: The biggest challenge or surprise happens right at the top of the mountain.",
                        "Ending (Resolution): The problem gets solved and we see what changed.",
                    ),
                    funFact = "Authors often draw a 'story map' before writing a single chapter so they don't lose track of clues!",
                    tryItActivity = "Invent a 3-sentence story about a squirrel who finds a tiny golden key in the park.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What is the 'setting' of a story?",
                            options = listOf("Where and when it takes place", "The back cover price", "Only the villain's name", "The font size"),
                            correctIndex = 0,
                            explanation = "Setting describes the time and place where the story happens.",
                        ),
                        QuizQuestion(
                            prompt = "Which part of a story has the most exciting turning point?",
                            options = listOf("Table of contents", "Climax", "Dedication", "Glossary"),
                            correctIndex = 1,
                            explanation = "The climax is the peak of excitement where the big problem is faced.",
                        ),
                        QuizQuestion(
                            prompt = "What happens in the resolution?",
                            options = listOf("The problem is solved", "The characters haven't met yet", "The book stops mid-sentence", "New titles are listed"),
                            correctIndex = 0,
                            explanation = "The resolution wraps up the problem at the end of the story.",
                        ),
                    ),
                ),
                Lesson(
                    id = "reading_2",
                    subjectId = "reading",
                    title = "Sparkly Adjectives & Verbs",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–10",
                    storyIntro = "Words paint pictures in a reader's mind! Swap plain words for vivid verbs and colorful adjectives to make sentences pop.",
                    keyPoints = listOf(
                        "Nouns name people, places, or things (like 'puppy' or 'island').",
                        "Verbs are action words that tell what happens (like 'zoomed', 'whispered', or 'splashed').",
                        "Adjectives describe nouns (like 'sparkling', 'gigantic', or 'cozy').",
                    ),
                    funFact = "Instead of saying 'walked', English has over 50 fun action verbs like 'tiptoed', 'marched', 'strolled', and 'bounded'!",
                    tryItActivity = "Rewrite 'The dog ran fast' using 1 awesome adjective and 1 vivid verb!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "In 'The fluffy kitten leaped high', which word is an adjective?",
                            options = listOf("kitten", "leaped", "fluffy", "the"),
                            correctIndex = 2,
                            explanation = "'Fluffy' describes the noun 'kitten', so it is an adjective.",
                        ),
                        QuizQuestion(
                            prompt = "Which word is an action verb?",
                            options = listOf("Rainbow", "Sprinted", "Bright", "Basket"),
                            correctIndex = 1,
                            explanation = "'Sprinted' shows action, making it a verb.",
                        ),
                        QuizQuestion(
                            prompt = "Which word is a noun?",
                            options = listOf("Library", "Cheerfully", "Jumped", "Tiny"),
                            correctIndex = 0,
                            explanation = "'Library' is a place, so it is a noun.",
                        ),
                    ),
                ),
                Lesson(
                    id = "reading_3",
                    subjectId = "reading",
                    title = "Detective Context Clues",
                    durationMinutes = 10,
                    ageBadge = "Ages 8–12",
                    storyIntro = "Ever spot a tricky new word in a book? You don't always need a dictionary — neighboring words leave helpful clues!",
                    keyPoints = listOf(
                        "Look at the sentence before and after the mystery word.",
                        "Watch for clue words like 'such as', 'meaning', or opposites like 'however'.",
                        "Try swapping in a word you know to see if the sentence still makes sense.",
                    ),
                    funFact = "Reading just 15 minutes a day during holidays introduces you to over 100,000 words every summer!",
                    tryItActivity = "Read 2 pages of any book and circle one new word, then guess its meaning from the sentence before checking!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "'The desert was arid, with cracked dry sand and zero rain for months.' What does 'arid' mean?",
                            options = listOf("Very wet", "Very dry", "Freezing cold", "Full of trees"),
                            correctIndex = 1,
                            explanation = "Clues like 'cracked dry sand' and 'zero rain' show 'arid' means very dry.",
                        ),
                        QuizQuestion(
                            prompt = "'Leo was famished after soccer, so he gobbled two big sandwiches.' What does 'famished' mean?",
                            options = listOf("Sleepy", "Very hungry", "Angry", "Bored"),
                            correctIndex = 1,
                            explanation = "Gobbling two sandwiches right after soccer tells us he was very hungry.",
                        ),
                        QuizQuestion(
                            prompt = "'Unlike her boisterous puppy, the cat slept quietly on the rug.' What does 'boisterous' mean?",
                            options = listOf("Noisy and energetic", "Invisible", "Quiet and shy", "Tiny"),
                            correctIndex = 0,
                            explanation = "The contrast word 'Unlike' compares the puppy to the cat sleeping quietly.",
                        ),
                    ),
                ),
            ),
        ),
        Subject(
            id = "world",
            title = "World & History",
            subtitle = "Continents, maps & wonders",
            accentColor = SkyCerulean,
            containerColor = SkySoft,
            icon = Icons.Rounded.Public,
            lessons = listOf(
                Lesson(
                    id = "world_1",
                    subjectId = "world",
                    title = "Seven Continents Voyage",
                    durationMinutes = 12,
                    ageBadge = "Ages 7–11",
                    storyIntro = "Pack your suitcase! Earth's land is divided into 7 giant continents surrounded by 5 vast oceans.",
                    keyPoints = listOf(
                        "The 7 continents are Asia, Africa, North America, South America, Antarctica, Europe, and Australia.",
                        "Asia is the largest continent by both land size and population.",
                        "Antarctica is the coldest, windiest continent and is covered in thick ice!",
                    ),
                    funFact = "Africa has more countries (54!) than any other continent on Earth.",
                    tryItActivity = "Point to North, South, East, and West in your house using where the sun rises (East) and sets (West)!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "Which continent is the largest by land area?",
                            options = listOf("Europe", "Asia", "Australia", "South America"),
                            correctIndex = 1,
                            explanation = "Asia is Earth's biggest continent.",
                        ),
                        QuizQuestion(
                            prompt = "Which icy continent sits around the South Pole?",
                            options = listOf("Antarctica", "Africa", "North America", "Europe"),
                            correctIndex = 0,
                            explanation = "Antarctica surrounds the South Pole and is home to emperor penguins.",
                        ),
                        QuizQuestion(
                            prompt = "How many continents are there on Earth?",
                            options = listOf("5", "6", "7", "9"),
                            correctIndex = 2,
                            explanation = "There are 7 continents across the globe.",
                        ),
                    ),
                ),
                Lesson(
                    id = "world_2",
                    subjectId = "world",
                    title = "Maps, Compasses & Symbols",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–10",
                    storyIntro = "Before GPS satellites, explorers used paper maps and magnetic compasses to cross oceans and deserts!",
                    keyPoints = listOf(
                        "A compass rose shows the four cardinal directions: North, East, South, and West.",
                        "A map legend (key) explains what tiny symbols like trees, tents, or bridges mean.",
                        "Map scale shows how real-world miles or kilometers shrink to fit on paper.",
                    ),
                    funFact = "A handy rhyme to remember North-East-South-West clockwise is: 'Never Eat Soggy Waffles!'",
                    tryItActivity = "Draw a treasure map of your living room with a compass rose and a legend showing 3 symbols!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What part of a map explains what symbols and colors mean?",
                            options = listOf("Map legend (key)", "The spine", "The margin", "The border"),
                            correctIndex = 0,
                            explanation = "The legend or key unlocks what every symbol stands for.",
                        ),
                        QuizQuestion(
                            prompt = "If you face North, which direction is directly behind you?",
                            options = listOf("East", "West", "South", "Northeast"),
                            correctIndex = 2,
                            explanation = "South is directly opposite North.",
                        ),
                        QuizQuestion(
                            prompt = "Where does the sun appear to rise each morning?",
                            options = listOf("East", "West", "North", "South"),
                            correctIndex = 0,
                            explanation = "Because Earth spins eastward, the sun rises in the East.",
                        ),
                    ),
                ),
                Lesson(
                    id = "world_3",
                    subjectId = "world",
                    title = "Ancient Wonders Time Travel",
                    durationMinutes = 12,
                    ageBadge = "Ages 8–12",
                    storyIntro = "Hop in a time machine to visit amazing stone monuments built thousands of years ago without modern cranes!",
                    keyPoints = listOf(
                        "The Great Pyramid of Giza in Egypt was built over 4,500 years ago and still stands today.",
                        "The Great Wall of China stretches thousands of kilometers across mountains and valleys.",
                        "Ancient builders used ramps, levers, ropes, and clever teamwork to lift giant stones.",
                    ),
                    funFact = "The Great Pyramid of Giza was the tallest human-made structure on Earth for over 3,800 years!",
                    tryItActivity = "Stack paper cups or blocks into a pyramid and count how many blocks form the wide base vs the top.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "Where is the Great Pyramid of Giza located?",
                            options = listOf("Egypt", "Brazil", "Japan", "Norway"),
                            correctIndex = 0,
                            explanation = "The Great Pyramid stands near Cairo in Egypt.",
                        ),
                        QuizQuestion(
                            prompt = "Which ancient wonder stretches across northern China?",
                            options = listOf("Colosseum", "Great Wall", "Petra", "Machu Picchu"),
                            correctIndex = 1,
                            explanation = "The Great Wall of China winds across hills and deserts.",
                        ),
                        QuizQuestion(
                            prompt = "What simple machines helped ancient workers move heavy stones?",
                            options = listOf("Ramps and levers", "Jet engines", "Steam trains", "Solar panels"),
                            correctIndex = 0,
                            explanation = "Ramps, rollers, and levers multiply human strength!",
                        ),
                    ),
                ),
            ),
        ),
        Subject(
            id = "arts",
            title = "Creative Arts",
            subtitle = "Color mixing, music & craft ideas",
            accentColor = LavenderPlum,
            containerColor = LavenderSoft,
            icon = Icons.Rounded.Palette,
            lessons = listOf(
                Lesson(
                    id = "arts_1",
                    subjectId = "arts",
                    title = "Color Wheel Magic",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–10",
                    storyIntro = "Did you know three special paint colors can mix together to create almost every color in a rainbow?",
                    keyPoints = listOf(
                        "Red, yellow, and blue are classic primary colors in painting.",
                        "Mixing two primary colors makes secondary colors: orange, green, and purple!",
                        "Warm colors (red, orange, yellow) feel sunny; cool colors (blue, green, violet) feel calm like ocean water.",
                    ),
                    funFact = "Adding white to a color makes a lighter 'tint' (like pink), while adding black makes a darker 'shade'!",
                    tryItActivity = "Use crayons or markers to layer yellow over blue and watch green appear!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What color do you get when you mix yellow and blue paint?",
                            options = listOf("Orange", "Green", "Purple", "Pink"),
                            correctIndex = 1,
                            explanation = "Yellow + blue creates green!",
                        ),
                        QuizQuestion(
                            prompt = "Which set lists only cool colors?",
                            options = listOf("Red and orange", "Blue and green", "Yellow and red", "Gold and coral"),
                            correctIndex = 1,
                            explanation = "Blues and greens remind us of cool water and shade.",
                        ),
                        QuizQuestion(
                            prompt = "What happens when you mix red and yellow?",
                            options = listOf("Orange", "Purple", "Brown", "Silver"),
                            correctIndex = 0,
                            explanation = "Red and yellow combine to make bright orange.",
                        ),
                    ),
                ),
                Lesson(
                    id = "arts_2",
                    subjectId = "arts",
                    title = "Rhythm & Musical Beats",
                    durationMinutes = 10,
                    ageBadge = "Ages 6–11",
                    storyIntro = "Tap your foot or clap your hands — music is built from steady beats and fun patterns of long and short sounds!",
                    keyPoints = listOf(
                        "Beat is the steady pulse in a song, just like a ticking clock or a heartbeat.",
                        "Rhythm is the pattern of long and short notes dancing on top of the beat.",
                        "Tempo tells musicians how fast or slow to play — 'Allegro' means fast and cheerful!",
                    ),
                    funFact = "You can turn everyday kitchen bowls and wooden spoons into a percussion drum kit!",
                    tryItActivity = "Clap 'Ta, Ta, Ti-Ti, Ta' four times in a row to feel a 4-beat rhythm pattern.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What does 'tempo' describe in music?",
                            options = listOf("How fast or slow the music goes", "The color of the guitar", "The title of the album", "How many singers are on stage"),
                            correctIndex = 0,
                            explanation = "Tempo is the speed of the beat in a piece of music.",
                        ),
                        QuizQuestion(
                            prompt = "Which instrument family includes drums, tambourines, and xylophones?",
                            options = listOf("Percussion", "Strings", "Brass", "Woodwinds"),
                            correctIndex = 0,
                            explanation = "Percussion instruments make sound when struck, shaken, or tapped.",
                        ),
                        QuizQuestion(
                            prompt = "What is the steady pulse of a song called?",
                            options = listOf("Beat", "Lyrics", "Echo", "Volume"),
                            correctIndex = 0,
                            explanation = "The beat is the steady pulse you clap or march to.",
                        ),
                    ),
                ),
                Lesson(
                    id = "arts_3",
                    subjectId = "arts",
                    title = "Origami & Symmetry",
                    durationMinutes = 12,
                    ageBadge = "Ages 7–12",
                    storyIntro = "One flat square of paper can transform into a boat, butterfly, or jumping frog using folds and symmetry!",
                    keyPoints = listOf(
                        "Symmetry means both halves match like a reflection in a mirror.",
                        "In origami, crisp creases make every fold line up neatly.",
                        "Even tiny paper airplanes use symmetry on their wings so they glide straight!",
                    ),
                    funFact = "The word 'origami' comes from Japanese: 'ori' means folding, and 'kami' means paper.",
                    tryItActivity = "Fold a square sheet diagonally into a triangle, then fold a paper sailboat or hat!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What does 'origami' mean?",
                            options = listOf("Paper folding", "Clay sculpting", "Watercolor painting", "Wood carving"),
                            correctIndex = 0,
                            explanation = "'Ori' means folding and 'kami' means paper in Japanese.",
                        ),
                        QuizQuestion(
                            prompt = "If a butterfly's left wing matches its right wing, it has:",
                            options = listOf("Symmetry", "Friction", "Gravity", "Volume"),
                            correctIndex = 0,
                            explanation = "Mirror-image matching halves show symmetry!",
                        ),
                        QuizQuestion(
                            prompt = "Why should a paper airplane's left and right wings match?",
                            options = listOf("To fly balanced and straight", "To change its color", "To make it heavier", "To remove air"),
                            correctIndex = 0,
                            explanation = "Symmetric wings keep lift balanced on both sides.",
                        ),
                    ),
                ),
            ),
        ),
        Subject(
            id = "coding",
            title = "Coding & Logic",
            subtitle = "Patterns, loops & brain teasers",
            accentColor = CobaltBlue,
            containerColor = CobaltSoft,
            icon = Icons.Rounded.Code,
            lessons = listOf(
                Lesson(
                    id = "coding_1",
                    subjectId = "coding",
                    title = "Step-by-Step Algorithms",
                    durationMinutes = 10,
                    ageBadge = "Ages 7–12",
                    storyIntro = "Computers are super fast, but they need exact step-by-step instructions — called algorithms — just like a cooking recipe!",
                    keyPoints = listOf(
                        "An algorithm is an ordered list of steps to finish a task.",
                        "Order matters! You put on socks BEFORE shoes, not shoes before socks.",
                        "Fixing a mistake in your steps is called 'debugging'.",
                    ),
                    funFact = "The word 'bug' became famous in 1947 when engineers actually found a real moth stuck inside an early computer!",
                    tryItActivity = "Write 4 exact steps for making a peanut-butter or jam sandwich and check if any step is missing!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What is an algorithm?",
                            options = listOf("A step-by-step set of instructions", "A computer keyboard", "A type of battery", "A broken screen"),
                            correctIndex = 0,
                            explanation = "Algorithms are clear, ordered steps to solve a problem or complete a task.",
                        ),
                        QuizQuestion(
                            prompt = "What do programmers call finding and fixing a mistake in code?",
                            options = listOf("Debugging", "Downloading", "Charging", "Printing"),
                            correctIndex = 0,
                            explanation = "Debugging means hunting down and fixing errors (bugs).",
                        ),
                        QuizQuestion(
                            prompt = "Why does step order matter in an algorithm?",
                            options = listOf("Wrong order can break the result", "Computers only read backwards", "Steps can be skipped randomly", "Colors disappear"),
                            correctIndex = 0,
                            explanation = "Putting steps in the wrong sequence causes unexpected results.",
                        ),
                    ),
                ),
                Lesson(
                    id = "coding_2",
                    subjectId = "coding",
                    title = "Super-Speed Loops",
                    durationMinutes = 10,
                    ageBadge = "Ages 7–12",
                    storyIntro = "Imagine telling a robot 'step forward' 100 times in a row. Loops let you write it once and repeat automatically!",
                    keyPoints = listOf(
                        "A loop repeats the same action multiple times without rewriting it.",
                        "Counted loops repeat a set number of times (e.g., Repeat 4 times: walk & turn 90° to draw a square!).",
                        "Loops save time and keep instructions tidy and easy to read.",
                    ),
                    funFact = "Video games run a giant loop 60 times every single second to update characters, scores, and animations!",
                    tryItActivity = "Teach a sibling a 3-step dance move and shout 'LOOP x4!' to perform it together.",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "What does a loop do in coding?",
                            options = listOf("Repeats instructions automatically", "Deletes the whole file", "Turns off the monitor", "Sends mail"),
                            correctIndex = 0,
                            explanation = "Loops repeat a block of steps without typing them over and over.",
                        ),
                        QuizQuestion(
                            prompt = "To draw a 4-sided square, how many times should 'move forward + turn corner' repeat?",
                            options = listOf("2 times", "3 times", "4 times", "10 times"),
                            correctIndex = 2,
                            explanation = "4 sides and 4 corners require 4 repeats!",
                        ),
                        QuizQuestion(
                            prompt = "What is a big benefit of using loops?",
                            options = listOf("Shorter, cleaner instructions", "Slower batteries", "Bigger screens", "No numbers needed"),
                            correctIndex = 0,
                            explanation = "Loops keep code compact and prevent copy-paste mistakes.",
                        ),
                    ),
                ),
                Lesson(
                    id = "coding_3",
                    subjectId = "coding",
                    title = "Secret Binary Language",
                    durationMinutes = 12,
                    ageBadge = "Ages 8–12",
                    storyIntro = "Inside every tablet and phone, tiny switches are either ON (1) or OFF (0). That two-symbol code is called binary!",
                    keyPoints = listOf(
                        "Binary uses only two digits: 0 and 1.",
                        "Each 0 or 1 is called a 'bit', and 8 bits together make a 'byte'.",
                        "Patterns of 0s and 1s can represent numbers, letters, music, and colorful pixels!",
                    ),
                    funFact = "In binary, the number 5 is written as 101 (one 4 + zero 2s + one 1)!",
                    tryItActivity = "Use a flashlight for ON (1) and OFF (0) to send a secret 3-bit signal across the room!",
                    quiz = listOf(
                        QuizQuestion(
                            prompt = "Which two digits are used in binary code?",
                            options = listOf("0 and 1", "1 and 2", "0 and 9", "A and B"),
                            correctIndex = 0,
                            explanation = "Binary is base-2, using only 0 (OFF) and 1 (ON).",
                        ),
                        QuizQuestion(
                            prompt = "How many bits make one byte?",
                            options = listOf("4", "8", "12", "16"),
                            correctIndex = 1,
                            explanation = "8 bits grouped together equal 1 byte.",
                        ),
                        QuizQuestion(
                            prompt = "What does a single 0 or 1 in binary get called?",
                            options = listOf("A bit", "A pixel", "A loop", "A folder"),
                            correctIndex = 0,
                            explanation = "Short for 'binary digit', a single 0 or 1 is called a bit.",
                        ),
                    ),
                ),
            ),
        ),
    )

    val badges: List<ExplorerBadge> = listOf(
        ExplorerBadge(
            id = "first_lesson",
            title = "Curious Starter",
            description = "Complete your very first holiday lesson.",
            requirementLabel = "1+ Lesson",
        ),
        ExplorerBadge(
            id = "routine_builder",
            title = "Time Planner",
            description = "Add at least 3 activities to your daily timetable.",
            requirementLabel = "3+ Routines",
        ),
        ExplorerBadge(
            id = "daily_checker",
            title = "Routine Star",
            description = "Check off 3 routine activities in a single day.",
            requirementLabel = "3 Done Today",
        ),
        ExplorerBadge(
            id = "star_collector",
            title = "Star Collector",
            description = "Earn 50 or more total stars across lessons and routines.",
            requirementLabel = "50+ Stars",
        ),
        ExplorerBadge(
            id = "quiz_master",
            title = "Quiz Ace",
            description = "Get a perfect 3/3 score on any lesson quiz.",
            requirementLabel = "Perfect Quiz",
        ),
        ExplorerBadge(
            id = "holiday_scholar",
            title = "Holiday Scholar",
            description = "Complete at least 5 lessons across subjects.",
            requirementLabel = "5+ Lessons",
        ),
    )

    fun getSubject(subjectId: String): Subject? =
        subjects.firstOrNull { it.id == subjectId }

    fun getLesson(lessonId: String): Lesson? =
        subjects.flatMap { it.lessons }.firstOrNull { it.id == lessonId }
}
