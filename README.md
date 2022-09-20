# Airport Enhanced

Android app for airport's flights schedule view and management. 

* [Project's website](https://airport.serguun42.ru/) contains
    * Swagger API docs
    * Redoc API docs
    * Built application (in `.apk` format)
* [Airport Frontend repo](https://github.com/serguun42/airport-frontend)

### Generate single Word file with all the code

1. Generate markdown file with [Node.js (_v16+_)](https://nodejs.org/) – `node java-combined-markdown.js`
2. Convert it to `.docx` format with [pandoc](https://pandoc.org/)
   – `pandoc -f markdown -t docx -o Generated.docx CombinedJava.md`

---

### [BSL-1.0 License](./LICENSE)
