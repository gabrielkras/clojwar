# API DOCUMENTATION FOLDER

+ This folder contains the **ClojWar API** documentation, so this 
documentation is generated by Aglio API library, its just a 
**apib file** converts to a **HTML file**.

+ If you want or need to update de API file, you just need to 
update the apidoc.apib file and executes the following procedures:

Be sure you have aglio and npm installed on your machine! If you don't have, 
just execute the following command (for Linux Users).

#### Redhat Users
```
# COMMAND TO INSTALL NPM
$ yum install npm 

# COMMAND TO INSTALL AGLIO GLOBALLY
$ npm install -g aglio

# COMMAND TO REGENERATE THE HTML FILE
$ aglio -i <your_apib_file> --theme-template triple -o <your_html_file>

```

#### Ubuntu Users
```
# COMMAND TO INSTALL NPM
$ apt-get install npm 

# COMMAND TO INSTALL AGLIO GLOBALLY
$ npm install -g aglio

# COMMAND TO REGENERATE THE HTML FILE
$ aglio -i <your_apib_file> --theme-template triple -o <your_html_file>

```