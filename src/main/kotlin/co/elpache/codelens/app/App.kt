package co.elpache.codelens.app


import co.elpache.codelens.Factory
import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.CodeSmellsUseCases
import co.elpache.codelens.useCases.EvolutionUseCases
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean


@SpringBootApplication
class CodeLensApp {

  private val logger = KotlinLogging.logger {}


  @Bean
  fun factory(context: ApplicationContext) = Factory(context = context)

  @Bean
  fun codeExplorerUseCases(factory: Factory): CodeExplorerUseCases {
    val ce = CodeExplorerUseCases(factory);

    return ce;
  }

  @Bean
  fun smellUseCases(ce: CodeExplorerUseCases) = CodeSmellsUseCases(ce)

  @Bean
  fun evolutionUseCases(factory: Factory): EvolutionUseCases {
    val ec = EvolutionUseCases(factory)

    GlobalScope.launch {
      ec.preloadCommits(20)
    }

    return ec
  }

}

fun main(args: Array<String>) {
  runApplication<CodeLensApp>(*args)
}