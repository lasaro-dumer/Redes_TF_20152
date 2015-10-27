OUTDIR	= .
SRCDIR	= src
MAIN_JAVA_FILE_NAME = ${SRCDIR}/Simulador.java
MAIN_CLASS_FILE_NAME = Simulador.class
JAR_FILE_NAME = Simulador.jar

all: jarf cleanC

jarf: build
	jar cmvf META-INF/MANIFEST.MF ${JAR_FILE_NAME} *.class

build:
	javac -d ${OUTDIR} -cp ${OUTDIR} -sourcepath ${SRCDIR} ${MAIN_JAVA_FILE_NAME}

clean: cleanC
	$(RM) ${JAR_FILE_NAME} 

cleanC:
	$(RM) *.class
