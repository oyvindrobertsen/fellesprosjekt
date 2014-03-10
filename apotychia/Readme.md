# Hvordan komme i gang

## Installere virtualbox og Vagrant

For å være sikre på at alle jobber i samme utviklermiljø, bruker vi Vagrant, som lar oss beskrive vha. en enkelt fil hvordan miljøet skal se ut, i kombinasjon med Virtualbox, en virtualiserer. For å komme i gang, må vi installere disse.

### Virtualbox

Gå til `https://www.virtualbox.org/wiki/Downloads` og last ned installeren til ditt OS. Installer.

### Vagrant

Gå til `http://www.vagrantup.com/downloads.html` og last ned versjonen som tilhører ditt OS. Kjør installer.

## Bruk

All koden som Apotychia består av ligger i mappen `fellesprosjekt/apotychia/`, og kan redigeres uten at det egentlig er nødvendig at vi alle jobber med samme utviklingsmiljø. Det er når vi skal teste at problemer kan oppstå. Hvis noen prøver å teste med Java 1.6 installert, mens koden forventer 1.7, vil det skape problemer. Derfor kan det være lurt følge disse stegene før hver gang du ønsker å implementere noe.

1. Åpne terminal/cmd, naviger til mappen der du har klonet repoet.
2. Naviger inn i `apotychia`-mappen.
3. Kjør kommandoen `vagrant up`, den kommer til å bruke ganske lang tid.
4. Kjør kommandoen `vagrant ssh`, denne kobler deg til den virtuelle maskinen som kjører.
5. Kjør `cd /vagrant/apotychia/`, deretter `gradle wrapper && ./gradlew bootRun`. Applikasjonen skal nå kjøre, og du kan åpne `http://localhost:8080/greeting` i nettleseren din.
6. `vagrant suspend` for å midlertidig avslutte maskinen, `vagrant resume` for å komme tilbake.

For å fjerne alle spor etter den virtuelle maskinen, kjør `vagrant destroy`.
