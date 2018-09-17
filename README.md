Increase version on `build.sbt` then:
```
sbt dist
cf target -o wavein.ch -s Elle
cf push elle-web -p target\universal\
```