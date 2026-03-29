rootProject.name = "han-note"

include("platform")
include("framework:common")
include("framework:han-note-spring-boot-starter-biz-operationlog")
include("framework:han-note-spring-boot-starter-biz-context")
include("framework:han-note-spring-boot-starter-jackson")

include("han-note-auth")
include("han-note-gateway")
include("han-note-oss:han-note-oss-api")
include("han-note-oss:han-note-oss-biz")