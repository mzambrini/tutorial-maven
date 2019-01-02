## Descrizione
Questo progetto mostra uno dei possibili approcci per gestire progetti di tipo Maven avendo Git come repository e in un contesto di CI/CD come quelli di GitLab/GitHub.

### Incompatibilità tra Maven e Git
In letteratura è noto come vi siano dei problemi di coesistenza tra Maven e Git; per i nostri scopi, ne andremo ad identificare solo due.

Il primo riguarda il modello di branching di Git.
In sostanza, ogni volta che si devono effettuare modifiche al codice sorgente, bisogna effettuare un'operazione di branching.
Per una questione di consistenza, il numero di versione del progetto maven deve essere modificato, ovvero bisogna intervenire sui sorgenti (nello specifico sui file di tipo **pom.xml**).
Tale operazione, se non eseguita in automazione, rischia di generare errori potenzialmente devastanti. 

Altro problema è quello rappresentato dal [plugin di rilascio fornito da Maven](http://maven.apache.org/maven-release/maven-release-plugin/ "Maven Release Plugin"). 
 Tale plugin esegue una serie di operazioni in sequenza che vanno ad impattare sul VCS e sul repository degli artefatti (Artifactory nel nostro caso). Tali operazioni violano palesemente il principio DRY (Don't repeat yourself) e sono incompatibili con la natura stessa di Git
 
### La soluzione proposta
La soluzione in questione utilizza il [Gitflow Maven Plugin](https://github.com/aleksandr-m/gitflow-maven-plugin "Gitflow Maven Plugin").
E' un plugin che permette di applicare il GitFlow (comprese le varianti Github e GitLab) ad un progetto Maven.

L'idea è la semplice: quando si deve creare un branch (sia esso di tipo **feature**, **hotfix** o **release**), si deve eseguire uno specifico goal maven fornito dal plugin.

Tale operazione deve essere eseguita in un ambiente opportunamente configurato con Maven e Git.
Il plugin, in base alla configurazione fonrita, provvederà a creare sul repository Git locale il branch richiesto, a modificare coerentemente i numeri di versione dei vari pom, a pushare le modifiche sul repository remoto.

Nello specifico, vedremo i passi necessari per eseguire una release; gli altri tipi di goal sono concettualmente simili.

#### Modifiche al file pom.xml
Deve essere modificato il solo file pom.xml di root (nel caso di progetti multimodulo, negli altri casi vi sarà un solo file pom.xml).
All'interno della sezione **<build><plugins>** deve essere aggiunto il seguente spezzonde di codice xml: 

```xml
  <plugin>
    <groupId>com.amashchenko.maven.plugin</groupId>
    <artifactId>gitflow-maven-plugin</artifactId>
    <version>1.10.0</version>
    <configuration>
      <installProject>false</installProject>
      <verbose>true</verbose>
      <fetchRemote>true</fetchRemote>
      <pushRemote>true</pushRemote>
      <skipTag>false</skipTag>
      <gitFlowConfig>
        <productionBranch>master</productionBranch>
        <developmentBranch>master</developmentBranch>
        <featureBranchPrefix>feature/</featureBranchPrefix>
        <releaseBranchPrefix>release/</releaseBranchPrefix>
        <hotfixBranchPrefix>hotfix/</hotfixBranchPrefix>
        <supportBranchPrefix>support/</supportBranchPrefix>
        <versionTagPrefix></versionTagPrefix>
        <origin>origin</origin>
      </gitFlowConfig>
    </configuration>
  </plugin>
```

Questa configurazione, nel nostro caso, è consistente con il flow di GitLab che non prevede i due branches **developer** e **master**, ma il solo **master**.

Il parametro **fetchRemote** posto a **true** indica che, in fase di inizializzazione del rilascio, verrà effettuata un'operazione di fetch  per confrontare repository locale e remoto al fine di intercettare immediatamente possibili inconsistenze.

Il parametro **pushRemote** posto a **true** indica che, una volta effettuate le operazioni di rilascio sul repository locale, queste verranno pushate su quello remoto.

Il parametro **skipTag** posto a **false** indica che sarà creato un tag di versione.

#### Preparazione al rilascio
Quando si è pronti per aprire il branch di rilascio, si deve eseguire il seguente comando:

```bash
mvn gitflow:release-start
```

Tale comando effettuerà (per la configurazione fornita in esempio) le seguenti operazioni: 
* Verifica dell'assenza di modifiche non committate
* Fetching del branch remoto (solitamente si chiama 'origin master')
* Comparazione tra il branch locale e quello remoto
* Verifica della consistenza delle dipendenze esterne del progetto (non devono esistere dipendenze di tipo SNAPSHOT)
* Richiesta del numero di versione da rilasciare (il default è quello derivato dall'analisi del pom)
* Creazione del branch locale **release/X.Y.Z** (dove X.Y.Z è il numero di versione)
* Modifica del numero di versione all'interno del/dei pom
* Commit su repository locale
* Push sul repository remoto

Il progetto locale sarà già configurato sul nuovo branch appena creato.

Da qui in poi, si può procedere con le sole modifiche dei sorgenti legate alle operazioni di rilascio

#### Esecuzione del rilascio
Quando si è pronti per finalizzare il rilascio, si può eseguire il seguente comando:

```bash
mvn gitflow:release-finish
```
Se tutto va bene, alla fine dell'esecuzione del goal ci troveremo in questa situazione:
* Verifica dell'assenza di modifiche non committate
* Checkout del branch **release/X.Y.Z**
* Confronto con il relativo branch remoto
* Verifica della consistenza delle dipendenze esterne del progetto (non devono esistere dipendenze di tipo SNAPSHOT)
* Esecuzione della fase di test di Maven
* Esecuzione del merge del branch **release/X.Y.Z** nel branch **master**
* Creazione del tag **X.Y.Z**
* Modifica del numero di versione all'interno del/dei pom per essere pronti alla nuova iterazione
* Commit delle modifiche
* Push su repository remoto
* Cancellazione del branch **release/X.Y.Z** sul repository locale
* Cancellazione del branch **release/X.Y.Z** sul repository remoto

#### Altre casistiche
Il plugin supporta differenti configurazioni.
E' possibile eseguire i goal in modalità non interattiva fornendo i valori richiesti all'interno di opportune variabili.
E' possibile effettuare tutte le modifiche in locale senza effettuare push e/o fetch, demandando ad un passo esplicito l'eventuale sincronizzazione col repository remoto.
E' possibile evitare la cancellazione del branch di cui si è fatta la merge

### Vantaggi e svantaggi di tale approccio
Il vantaggio principale di tale approccio è quello di automatizzare le attività di branching in modo da mantenere consistenti le versioni del progetto; inoltre il disaccoppiamento dal plugin ufficiale di Maven per il rilascio evita la serie di problematiche descritte all'inizio.

Lo svantaggio principale di questo approccio è quello di dover rinunciare (o almeno limitare) i meccanismo di merge request (GitLab) p pull request (GitHub).
Di certo, per il solo caso del rilascio, è un problema di tipo filosofico, più che pratico.



