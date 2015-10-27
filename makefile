OUTDIR	= .
SRCDIR	= .
MAIN_JAVA_FILE_NAME = Simulador.java
MAIN_CLASS_FILE_NAME = Simulador.class
JAR_FILE_NAME = Simulador.jar

all: jarf cleanC

jarf: build
	jar -cf ${JAR_FILE_NAME} ${MAIN_CLASS_FILE_NAME} 

build:
	javac -d ${OUTDIR} -cp ${OUTDIR} -sourcepath ${SRCDIR} ${MAIN_JAVA_FILE_NAME}

clean: cleanC
	$(RM) ${JAR_FILE_NAME} 

cleanC:
	$(RM) *.class
