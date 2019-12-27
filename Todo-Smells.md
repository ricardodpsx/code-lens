##Names
-[ ] Meaningful Names (Looking that names are in the dictionary)
-[ ] Word Cloud

##Functions
-[x] Long Parameters/Arguments list *
-[ ] Flag arguments *
-[ ] Output arguments  *
-[ ] Dead Function  	Single Level of Abstraction  (How close to each other are the nouns inside the functions) 
-[x] Long function *
-[ ] Cyclomatic Complexity   *
-[x] Too much Nesting *
-[ ] No side effects 

##Error Handling
-[ ] Exception instead of Error Codes 
	
##Classes
-[x] Long method list. *
-[ ] Cohesive *	(class/file is a dense graph)
-[ ] Static Coupling  * (Classes with more imports)
-[ ] Train Wreck x.y.z *
-[ ] Famelic Domains/Data classes with non trivial Methods (classes with only getters and setters plus big methods) *
-[ ] Encapsulation (Things are private for classes with big methods)
	
##Fowler’s Smells
-[ ] Duplicated code *
-[x] Long method *
-[ ] Large Class *
-[x] Long Parameter List *
-[ ] Divergent Change (a class/file/fun that changes too much) *
-[ ] Shotgun Surgery (A commit that touches a lot of classes at the same time) *
-[ ] Feature Envy * (A Class that is using too much of another)
-[ ] Middle Man
-[ ] Data Clumps* (Look at function arguments and see subsets)
-[ ] Case statements 	
- [ ] Lazy Class
-[ ] Temporarly Fields * 	Alternative classes with different interfaces
-[ ] Similar subclasses

#Mine
-[ ] Runtime Depth (How depth is the callstack to it)
-[ ] Dependencies of a function, file, class
