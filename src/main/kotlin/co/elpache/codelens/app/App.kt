package co.elpache.codelens.app


import co.elpache.codelens.Factory
import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.CodeSmellsUseCases
import co.elpache.codelens.useCases.EvolutionUseCases
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment


@SpringBootApplication
class CodeLensApp {
  private val logger = KotlinLogging.logger {}


  @Value("\${codelens.repo-tmp-dir}")
  lateinit var repoTmpDir: String

  @Bean
  fun factory(context: ApplicationContext) = Factory(path = repoTmpDir, context = context)

  @Bean
  fun codeExplorerUseCases(factory: Factory): CodeExplorerUseCases {
    val ce = CodeExplorerUseCases(factory)

    return ce;
  }

  @Bean
  fun smellUseCases(ce: CodeExplorerUseCases) = CodeSmellsUseCases(ce)

  @Bean
  fun evolutionUseCases(factory: Factory): EvolutionUseCases {
    return EvolutionUseCases(factory)
  }

  @Bean
  fun startupStuff(
    ec: EvolutionUseCases,
    env: Environment,
    codeExplorerUseCases: CodeExplorerUseCases
  ): InitializingBean {

    return InitializingBean {
      if (!env.activeProfiles.contains("test")) {
        codeExplorerUseCases.codeTree
        GlobalScope.launch {
          ec.preloadCommits(40)
        }
      }
    }
  }

}

fun main(args: Array<String>) {
  runApplication<CodeLensApp>(*args)
}