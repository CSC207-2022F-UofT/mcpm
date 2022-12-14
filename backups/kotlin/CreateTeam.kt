import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kohsuke.github.*
import java.io.File
import java.util.*
import kotlin.text.Charsets.UTF_8
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*


val GSON = Gson()
val HTTP = OkHttpClient()
val KTOR = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 5000
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 5)
    }
}

/**
 * fromJson patch for Kotlin
 *
 * @param T Type of the Json POJO class
 * @param json Json string
 */
inline fun <reified T> Gson.fromJson(json: String): T
{
    return fromJson(json, object: TypeToken<T>() {}.type)
}

/**
 * Gather a team for the final project with Data Science! xD
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @since 2022-09-10 00:51
 */
class CreateTeam()
{
    private val ghToken: String? = System.getenv("gh_token")
    private val tgToken: String? = System.getenv("tg_token")
    private val tgChatId: String? = System.getenv("tg_chat")
    private val github: GitHub

    init
    {
        if (ghToken.isNullOrBlank())
            throw RuntimeException("Please put gh_token in env")

        github = GitHubBuilder().withOAuthToken(ghToken).build()
    }

    suspend fun logTelegram(msg: String)
    {
        KTOR.get("https://api.telegram.org/$tgToken/sendMessage") {
            parameter("chat_id", tgChatId)
            parameter("text", msg)
        }
    }

    /**
     * Crawl a list of student who completed the 01-intro-to-java assignment
     *
     * @return List of GitHub usernames
     */
    fun crawlStudents(): List<String>
    {
        // Get all open repositories in CSC207 classroom
        val org = github.getOrganization("CSC207-2022F-UofT")
        val repos = org.repositories

        // Filter out 01-intro-to-java, get students' usernames
        // Example repo name: 01-intro-to-java-hykilpikonna
        return repos
            .filter { it.key.startsWith("01-intro-to-java") }
            .map { it.key.replace("01-intro-to-java-", "") }
    }

    /**
     * Cache a result of a function
     *
     * @param T The result class
     * @param cachePath Path to the cache location
     * @param obtain The function to obtain the result if the cache doesn't exist
     * @return The result of a function or the cached version of it
     */
    inline fun <reified T> cachedFn(cachePath: String, obtain: () -> T): T
    {
        // If cache exists, read from cache
        val cache = File(cachePath)
        if (cache.isFile) return GSON.fromJson(cache.readText(UTF_8))

        // Cache doesn't exist, run the function and write cache
        val result = obtain()
        cache.parentFile.mkdirs()
        cache.writeText(GSON.toJson(result))

        return result
    }

    /**
     * Crawl a GitHub user's information and store it in a cache
     *
     * @param name GitHub Username
     * @return GHUser: user info
     */
    fun cachedGithubUser(name: String) = cachedFn("data/github/users/$name") {
        github.getUser(name)
    }

    /**
     * Crawl a GitHub user's public repos and their organizations' public repos
     *
     * @param user GitHub user
     * @return List<GHRepository>: Public repos
     */
    fun cachedGithubUserRepos(user: GHUser) = cachedFn("data/github/user-repos/${user.login}") {
        user.repositories.values.toList()
    }

    /**
     * Get a GitHub user's stats from https://github.com/anuraghazra/github-readme-stats
     *
     * @param user Username
     * @return GithubStats: Stats
     */
    fun cachedGithubStats(user: String) = cachedFn("data/github/stats/$user") {
        // Get request
        val req = Request.Builder().url("https://github-readme-stats.vercel.app/api?username=$user" +
            "&role=OWNER,ORGANIZATION_MEMBER&include_all_commits=true").build()
        val html = HTTP.newCall(req).execute().body?.string()!!

        // Parse with regex
        val rank = Regex(".(?=</title>)").find(html)!!.value
        // val offsets = Regex("(?<=stroke-dashoffset: )\\d+?\\.\\d+?(?=;)").findAll(html)
        val stats = Regex("(?<=<desc id=\"descId\">).*?(?=</desc>)").find(html)!!.value
        val nums = stats.split(", ").map { it.split(" ").last().toInt() }

        GithubStats(rank, nums[0], nums[1], nums[2], nums[3], nums[4])
    }

    /**
     * Give a score to a GitHub user depending on the GitHub profile
     *
     * The score is a ratio of how someone's stats compare to mine. (i.e. I will have a score of 1)
     *
     * @param user User
     * @return Score
     */
    fun scoreUser(user: GHUser): Double = cachedFn("data/processed/score/${user.login}")
    {
        // Compute scores for followers count and repo count
        // val fo = user.followersCount / 144f
        // val repoCount = user.publicRepoCount / 94f

        // Compute score for days since the account is created
        // val days = ((System.currentTimeMillis() - user.createdAt.time) / 1000f / 60f / 60f / 24f) / 2183f

        // Compute score for number of repos
        // val tmpRepos = cachedGithubUserRepos(user)
        // val stars = tmpRepos.sumOf { it.stargazersCount }

        // Get stats
        val stats = cachedGithubStats(user.login)
        val stars = stats.stars / 143f
        val prs = stats.prs / 57f
        val issues = stats.issues / 100f
        val contrib = stats.contrib / 36f

        // Compute score
        // println("$fo, $repoCount, $days, $stars, $prs, $issues, $contrib")
        listOf(/*fo, repoCount, days, */stars, prs, issues, contrib).average()
    }

    fun scoreOrNull(name: String): GithubResult?
    {
        return try { GithubResult(name, scoreUser(cachedGithubUser(name))) }
        catch(e: GHFileNotFoundException) {
            println("Not found: $name")
            null
        }
    }

    /**
     * Get scores of each student
     *
     * @return List<GithubResult>
     */
    fun getScores() = crawlStudents().mapNotNull { scoreOrNull(it) }.sortedBy { it.score }.reversed()

    /**
     * Monitor for new students to submit scores
     *
     * @param period Delay period in seconds
     */
    suspend fun monitor(period: Double)
    {
        val lastStudents = crawlStudents().toMutableList()

        while (true)
        {
            delay((period * 1000).toLong())

            println("\nFetching students...")
            val curStudents = crawlStudents().filter { ! lastStudents.contains(it) }

            // No new students
            if (curStudents.isEmpty())
            {
                println("> No new student found")
                continue
            }

            // Yes new students >_<
            curStudents.mapNotNull { scoreOrNull(it) }.sortedBy { it.score }.forEach {
                println(it)
                logTelegram(it.toString())
            }

            lastStudents.addAll(curStudents)
        }
    }
}

data class GithubStats(
    val rank: String,
    // val percentile: Double,
    val stars: Int,
    val commits: Int,
    val prs: Int,    val issues: Int,
    val contrib: Int
)

fun Double.format(digits: Int) = "%.${digits}f".format(this)

data class GithubResult(
    val name: String,
    val score: Double
)
{
    override fun toString() = "$name: ${(score * 100).format(1)}%"
}

suspend fun main(args: Array<String>)
{
    val createTeam = CreateTeam()

    // println(createTeam.scoreUser(createTeam.cachedGithubUser("hykilpikonna")))
    println(createTeam.getScores().joinToString("\n"))
    createTeam.monitor(5.0)
    // createTeam.logTelegram("Test")
}

