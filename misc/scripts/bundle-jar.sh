cd "$(dirname "$0")" || exit 1
mvn -f ../../pom.xml package
printf "cd \"\$(dirname \"\$0\")\" || exit 1\njava -jar spotify-release-notifier.jar" \
> ../../bin/spotify-release-notifier-launcher.sh