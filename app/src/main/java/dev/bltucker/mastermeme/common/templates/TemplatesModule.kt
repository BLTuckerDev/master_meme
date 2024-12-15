package dev.bltucker.mastermeme.common.templates

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TemplatesModule {

    @Provides
    @Singleton
    fun provideMemeTemplateInitialier(@ApplicationContext context: Context,
                                      memeMetadataManager: MemeMetadataManager): MemeTemplateInitializer {
        val resources = context.resources
        val typedArray = resources.obtainTypedArray(
            resources.getIdentifier("meme_templates", "array", context.packageName)
        )

        val templateMap = buildMap {
            for (i in 0 until typedArray.length()) {
                val resourceId = typedArray.getResourceId(i, 0)
                if (resourceId != 0) {
                    val resourceName = resources.getResourceEntryName(resourceId)
                    put(resourceId, resourceName)
                }
            }
        }

        typedArray.recycle()

        return MemeTemplateInitializer(memeMetadataManager, templateMap)
    }

    @Provides
    @Singleton
    @Named("meme_metadata_map")
    fun provideMemeTemplateMetaData(): Map<String, MemeMetadata> {
        return mapOf(
            "14p2is" to MemeMetadata(
                friendlyName = "Drake Hotline Bling",
                searchTerms = setOf("drake", "reject", "accept", "no", "yes", "prefer", "choose", "better")
            ),
            "1_grus_plan" to MemeMetadata(
                friendlyName = "Gru's Plan",
                searchTerms = setOf("despicable me", "gru", "presentation", "plot twist", "backfire", "steps", "plan")
            ),
            "1jgrgn" to MemeMetadata(
                friendlyName = "Distracted Boyfriend",
                searchTerms = setOf("guy looking back", "jealous girlfriend", "wandering eyes", "temptation", "unfaithful")
            ),
            "1op9wy" to MemeMetadata(
                friendlyName = "Roll Safe",
                searchTerms = setOf("think about it", "smart", "guy tapping head", "cant lose", "clever", "big brain")
            ),
            "1otri4" to MemeMetadata(
                friendlyName = "One Does Not Simply",
                searchTerms = setOf("boromir", "lord of the rings", "lotr", "mordor", "walk", "impossible")
            ),
            "1yz6z4" to MemeMetadata(
                friendlyName = "Expanding Brain",
                searchTerms = setOf("mind", "expanding", "evolution", "ascend", "bigger", "brain", "intelligence", "progressive")
            ),
            "21ajtl" to MemeMetadata(
                friendlyName = "Woman Yelling at Cat",
                searchTerms = setOf("confused cat", "angry woman", "dinner table", "smudge", "cat", "housewives")
            ),
            "24zoa8" to MemeMetadata(
                friendlyName = "This Is Fine",
                searchTerms = setOf("dog", "fire", "everything fine", "burning", "calm", "crisis", "denial")
            ),
            "2bbctk" to MemeMetadata(
                friendlyName = "Monkey Puppet Side Eye",
                searchTerms = setOf("monkey", "puppet", "awkward", "look away", "nervous", "side glance")
            ),
            "2byuiy" to MemeMetadata(
                friendlyName = "Always Has Been",
                searchTerms = setOf("astronaut", "gun", "space", "earth", "wait", "conspiracy", "always")
            ),
            "2eb198" to MemeMetadata(
                friendlyName = "Bernie Asking for Support",
                searchTerms = setOf("bernie sanders", "financial support", "asking", "campaign", "request", "help")
            ),
            "2qx7sw" to MemeMetadata(
                friendlyName = "Batman Slapping Robin",
                searchTerms = setOf("batman", "robin", "slap", "shut up", "silence", "stop")
            ),
            "2reqtg" to MemeMetadata(
                friendlyName = "Surprised Pikachu",
                searchTerms = setOf("pokemon", "pikachu", "shocked", "surprised", "unexpected", "gasp")
            ),
            "2t8r9a" to MemeMetadata(
                friendlyName = "Disaster Girl",
                searchTerms = setOf("fire", "evil smile", "burning house", "little girl", "chaos", "smirk")
            ),
            "2w2e5e" to MemeMetadata(
                friendlyName = "Stonks",
                searchTerms = setOf("stocks", "stonks", "money", "profit", "business", "success", "finance")
            ),
            "34vt4i" to MemeMetadata(
                friendlyName = "Unsettled Tom",
                searchTerms = setOf("tom and jerry", "disturbed", "uncomfortable", "concerned", "worried", "cat")
            ),
            "3eqjd8" to MemeMetadata(
                friendlyName = "Buff Doge vs Cheems",
                searchTerms = setOf("dogs", "strong", "weak", "comparison", "then vs now", "past", "present")
            ),
            "3f0mvv" to MemeMetadata(
                friendlyName = "Spiderman Pointing",
                searchTerms = setOf("spiderman", "pointing", "identical", "same", "duplicate", "copy")
            ),
            "3igo27" to MemeMetadata(
                friendlyName = "Panik Kalm Panik",
                searchTerms = setOf("panic", "calm", "meme man", "emotions", "stress", "anxiety", "relief")
            ),
            "3x8soh" to MemeMetadata(
                friendlyName = "Success Kid",
                searchTerms = setOf("baby", "success", "victory", "achievement", "fist pump", "winning")
            ),
            "43iacv" to MemeMetadata(
                friendlyName = "They Don't Know",
                searchTerms = setOf("party", "corner", "lonely", "thinking", "social", "outsider")
            ),
            "49su9f" to MemeMetadata(
                friendlyName = "Trade Offer",
                searchTerms = setOf("trade", "offer", "receive", "deal", "tiktok", "negotiation")
            ),
            "58eyvu" to MemeMetadata(
                friendlyName = "Megamind Peeking",
                searchTerms = setOf("megamind", "peek", "no bitches", "questioning", "doubt", "suspicious")
            ),
            "59c1hh" to MemeMetadata(
                friendlyName = "Awkward Monkey Puppet",
                searchTerms = setOf("monkey", "puppet", "awkward", "uncomfortable", "nervous", "side eye")
            ),
            "64sz4u" to MemeMetadata(
                friendlyName = "Mr Incredible Becoming Uncanny",
                searchTerms = setOf("incredibles", "uncanny", "scary", "disturbing", "progression", "worse")
            ),
            "6rcrc1" to MemeMetadata(
                friendlyName = "Galaxy Brain",
                searchTerms = setOf("brain", "expanding", "intelligence", "smart", "genius", "ascension")
            ),
            "6u6ylb" to MemeMetadata(
                friendlyName = "Tuxedo Winnie Pooh",
                searchTerms = setOf("winnie", "pooh", "fancy", "sophisticated", "elegant", "posh", "upgrade")
            ),
            "7wxtd1" to MemeMetadata(
                friendlyName = "Disaster Girl",
                searchTerms = setOf("fire", "girl", "smiling", "evil", "chaos", "burning")
            ),
            "8c9dbh" to MemeMetadata(
                friendlyName = "Ancient Aliens Guy",
                searchTerms = setOf("history channel", "aliens", "conspiracy", "theory", "crazy hair")
            ),
            "boardroom_meeting_suggestion" to MemeMetadata(
                friendlyName = "Boardroom Meeting Suggestion",
                searchTerms = setOf("thrown out window", "corporate", "business", "meeting", "suggestion", "reject")
            ),
            "change_my_mind_" to MemeMetadata(
                friendlyName = "Change My Mind",
                searchTerms = setOf("debate", "crowder", "prove me wrong", "opinion", "challenge", "table")
            ),
            "clown_applying_makeup" to MemeMetadata(
                friendlyName = "Clown Applying Makeup",
                searchTerms = setOf("clown", "makeup", "progression", "foolish", "stepbystep", "transformation")
            ),
            "disaster_girl" to MemeMetadata(
                friendlyName = "Disaster Girl",
                searchTerms = setOf("fire", "evil smile", "burning house", "chaos", "little girl", "smirk")
            ),
            "epic_handshake" to MemeMetadata(
                friendlyName = "Epic Handshake",
                searchTerms = setOf("predator", "arm wrestle", "agreement", "alliance", "common ground", "unity")
            ),
            "hide_the_pain_harold" to MemeMetadata(
                friendlyName = "Hide The Pain Harold",
                searchTerms = setOf("old man", "fake smile", "suffering", "pretending", "pain", "harold")
            ),
            "i_bet_hes_thinking_about_other_women" to MemeMetadata(
                friendlyName = "I Bet He's Thinking About Other Women",
                searchTerms = setOf("bed", "couple", "thinking", "distracted", "boyfriend", "girlfriend", "thoughts")
            ),
            "is_this_a_pigeon" to MemeMetadata(
                friendlyName = "Is This a Pigeon",
                searchTerms = setOf("anime", "butterfly", "confused", "misidentification", "obvious", "wrong")
            ),
            "i_was_told_there_would_be" to MemeMetadata(
                friendlyName = "I Was Told There Would Be",
                searchTerms = setOf("office space", "promised", "expectation", "disappointment", "milton", "glasses")
            ),
            "jack_sparrow_being_chased" to MemeMetadata(
                friendlyName = "Jack Sparrow Being Chased",
                searchTerms = setOf("pirates", "caribbean", "running", "pursuit", "escape", "johnny depp")
            ),
            "left_exit_12_off_ramp" to MemeMetadata(
                friendlyName = "Left Exit 12 Off Ramp",
                searchTerms = setOf("car", "drifting", "choice", "decision", "sudden turn", "highway")
            ),
            "leonardo_dicaprio_cheers" to MemeMetadata(
                friendlyName = "Leonardo DiCaprio Cheers",
                searchTerms = setOf("great gatsby", "toast", "celebration", "leo", "champagne", "congratulations")
            ),
            "running_away_balloon" to MemeMetadata(
                friendlyName = "Running Away Balloon",
                searchTerms = setOf("responsibility", "floating away", "neglect", "priorities", "chasing", "letting go")
            ),
            "sad_pablo_escobar" to MemeMetadata(
                friendlyName = "Sad Pablo Escobar",
                searchTerms = setOf("narcos", "waiting", "lonely", "bored", "alone", "empty", "nothing to do")
            ),
            "scared_cat" to MemeMetadata(
                friendlyName = "Scared Cat",
                searchTerms = setOf("cat", "frightened", "shocked", "terrified", "fear", "surprise")
            ),
            "the_rock_driving" to MemeMetadata(
                friendlyName = "The Rock Driving",
                searchTerms = setOf("dwayne johnson", "rock", "car", "raised eyebrow", "surprise", "reaction")
            ),
            "third_world_skeptical_kid" to MemeMetadata(
                friendlyName = "Third World Skeptical Kid",
                searchTerms = setOf("african", "skeptical", "doubt", "questioning", "confused", "suspicious")
            ),
            "two_buttons" to MemeMetadata(
                friendlyName = "Two Buttons",
                searchTerms = setOf("choice", "decision", "sweating", "dilemma", "difficult", "buttons", "options")
            ),
            "waiting_skeleton" to MemeMetadata(
                friendlyName = "Waiting Skeleton",
                searchTerms = setOf("skeleton", "waiting", "forgotten", "eternal", "dead", "forever", "patience")
            )
        )
    }

}