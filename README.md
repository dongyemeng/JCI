JCI
===
Java Interface of the Colorado Richly Annotated Full Text (CRAFT) corpus.

This project includes the following experiments:
1. In ExpSet1.java
	1) (a) print parent-child pairs for each ontology
	   (b) plot the log of # of parent term context words vs. the log of # of child term context words (base 2)
	   
	2) Compute the # of terms for each ontology
	
	3) Generate labeled and unlabeled ARFF files of terms SO: 0001059 and SO: 0000041
	
	4) Make plot of the averaged # of occurrence vs depth plot for terms in each ontology
	
	5) Find text strings that can be expressed by more than one terms in an ontology, and write them into a file
	
2. In ExpSet2.java
	1) Make plot of 1uantiles versus cosine similarity scores of pair of parent term and child term for different distances of an ontology in CRAFT corpus
	
Run the main method in ExpSet1 and ExpSet2 class. To turn on an experiment, set the boolean variable of it be true before running.