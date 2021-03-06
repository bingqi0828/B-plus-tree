JFLAGS = -g
JC = javac
JVM = java
FILE=

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Key.java \
	Node.java \
	IndexNode.java \
	LeafNode.java \
	Tree.java \
	bplustree.java

MAIN = bplustree

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(Main)

clean:
	$(RM) *.class

