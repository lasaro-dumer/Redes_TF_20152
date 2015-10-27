OUTDIR	= .
SRCDIR	= .
MAIN_JAVA_FILE_NAME = Simulador.java
MAIN_CLASS_FILE_NAME = Simulador.class
JAR_FILE_NAME = Simulador.jar

all: jarf

jarf: build
	jar -cf ${JAR_FILE_NAME} ${MAIN_CLASS_FILE_NAME} 

build:
	javac -d ${OUTDIR} -cp ${OUTDIR} -sourcepath ${SRCDIR} ${MAIN_JAVA_FILE_NAME}

clean:
	$(RM) *.class ${JAR_FILE_NAME} 
