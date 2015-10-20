OUTDIR	= .
SRCDIR	= .
MAIN_JAVA_FILE_NAME = Simulador.java

default:
	javac -d ${OUTDIR} -cp ${OUTDIR} -sourcepath ${SRCDIR} ${MAIN_JAVA_FILE_NAME}

clean:
	$(RM) *.class
