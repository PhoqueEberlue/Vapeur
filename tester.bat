cls
echo OFF
echo compilation...
javac -d bin src/*.java
echo execution...
cd bin
echo Utilisateur:
java TestsUtilisateur
cd ..