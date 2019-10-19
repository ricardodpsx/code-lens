-[x] Show element in hierarchy
-[x] Highlight code inside a file
-[x] Show a File AST View
-[x] Style improvements, make code entities selectable and navigable with a side panel. #style
-[x] Functions/methods: num of lines, arguments, depth/nesting #functions
-[x] On Search Code metrics //https://react-google-charts.com/ #charts
-[x] Add support for Javascript #languageSupport
-[x] Make > to be more consistent with the css gramatic #search
-[x] Select by name #search
-[x] Support unquoted integers
-[x] Add selector like . for name #search
-[x] Simplify/unify common types like fun, class file round #1 etc #search
    fun, argument, class, call, import,  binding (var, val etc)   
-[x] Simplify/unify trees (JS)
    -[x] class: membersCount, propertiesCount, methodsCount, constructorsCount.
    -[x] file: name, lines, functionsCount, classesCount, bidingsCount
    -[x] fun: paramsCount, lines    
    -[x] call: startsWith, args, arg, argsCount 
    -[ ] if, loop
-[x] Simplify/unify trees (Kotlin)
    -[ ] class: members, properties, methods, constructors.
    -[ ] file: name, fileName, lines, functionsCount, classesCount, bidingsCount
    -[ ] fun: paramsCount, lines    
    -[ ] call: startsWith, args, arg, argsCount   
    -[ ] if, loop 
 
-[ ] Add history of a file node #evolution
     We want to see if the code got better or worst in between commits
     -[x] Core support
     -[ ] UI
 
-[ ] Basic sintax highlighting
-[ ] Code Starting with #Search
-[ ] Integrate analytics with search Results 
     When you select a frequency box it should show the selected functions
-[ ] On packages, see Dependencies etc #cleanCode
-[ ] Add Rules/Threshold #evolution
-[ ] Add  Root element selector #search
-[ ] Add References navigator #metrics
-[ ] Duplicated code #metrics
-[ ] Fuzzy/Content Search
-[ ] Stable ID's for functions and classes so that trees can be reconstructed more optimally
-[ ] Unequality selectors, search from current node for optimal/correct subQueries, support * (wildcard) #search
-[ ] JS Parser is too slow, probably because of the process up time #optimization
-[ ] Refresh single node, Instead of reload everything #optimization
-[ ] Make analytics (Expensive parameters like depth) to be only available for queries. 
        That way data don't have to be mutable
-[ ] Make AST More resilient
-[ ] Reload button


IceBox:
-[ ] Select other nodes than the leaf: 
    Example if I do a query "a b c"  I should be able to retrieve the nodes that matched a, b and c #search
-[ ] Support multiple 'class search' example fun#helloWorld
-[ ] Support 'kind' search, example: .fun.inline (A function that has class inline)  #search 
-[ ] File search should not expand when is not neccesary. (Optimize searcher) #search    
-[ ] Simplify parser into execution mode instead of data structure mode?
-[ ] Support Big Directories (Avoid code searching etc in non code files, optimize memory use)
-[ ] Make searchs like constructor fun[name=']
-[ ] Make searchs like fun#myFunction possible (Multi Kinds) #search
     
-[ ] Command line for Queries and Stats
-[ ] Query items showin in the Graph #charts
-[ ] Evolution of Metrics
-[ ] Branching #softwareMetrics
-[ ] Cyclomatic Complexity #softwareMetrics
-[ ] On classes... #cleanCode
-[ ] Fowler Smells #Smells
-[ ] Naming and Semantics
-[ ] Linguistic Similarity
-[ ] Improve AST View (Side pannel with more info)
-[ ] Better highlighting: when a code entity is selected it should highlight in the code
-[ ] Improve navigation: make mouse pointer selection compatible with search results highlights
-[ ] Make on Hover noticeable.
-[ ] Modularization
-[ ] IntelliJ plugin
-[ ] Integration with lint tools and code coverage

