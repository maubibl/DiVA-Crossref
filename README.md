Detta repositorie innehåller script och scheman som kan användas för att omvandla och deponera metadata från DIVA till Crossref. Scripten hanterar än så länge endast avhandlingar och rapporter:

## Innehåll:
### doireg.py: 
ett pythonscript som omvandlar XML filer i DIVA Mods till crossref XML och deponerar de omvandlade filerna hos Crossref och registrerar DOI.
### doireg_dry.py: 
detta script omvandlar XML från DiVA Mods till crossref XML, men skickar inte filerna till Crossref.
### DiVA-CrossRef.xslt: 
En XSLT fil som definierar omvandlingsreglerna från DiVA MODS till Crossref kompatibel XML.
### Doireg.java och Doireg_dry.java: 
Samma funktionalitet som i pythonscripten ovan, men skrivna i Java.

För att doireg.py och Doireg.java ska fungera behöves även crossref-upload-tool.jar laddas ner, finns tillgängligt här: https://www.crossref.org/documentation/register-maintain-records/direct-deposit-xml/https-post-using-java-program/

Observera att java måste vara installerat även om du använder pythonscripten.

## Konfiguration:
### doireg.py/doireg.java
Uppdatera följande variabler:

username: Ditt CrossRef användarnamn

password: Ditt CrossRef lösenord

jar_path/jarpath: Path till crossref-upload-tool.jar

### DiVA-CrossRef.xslt
Uppdatera depositor_name och email_adress i depositor elementet. 

## Tillvägagångssätt:
Ladda ner poster på de publikationer du vill registrera DOI från DIVA i formatet DiVA mods. Döp filen till export.xml och placera i samma mapp som scripten. Kör scriptet. En bekräftelse på deponeringen kommer mailas till den epostadress som angetts i DiVA-CrossRef.xslt.

Du kan ha flera publikationer i samma fil, men du kan inte blanda avhandlingar och rapporter (däremot kan såväl licentiat avhandlingar och doktorsavhandlingar, oavsett om de är monografier eller sammanläggningsavhandlingar). 

Observera att publikationer med dokumenttyp "Samlingsverk (redaktörskap)" i DiVA kommer att skickas till Crossref som rapport med redaktörer av skripten.
