Sprint: Problemas reportados pelo SonarQube

Resumo rápido:
- Total de issues exportadas: veja /tmp/sonar_issues.txt
- Severidades principais encontradas: BLOCKER, CRITICAL, MAJOR, MINOR, INFO

Lista (arquivo:linha — severidade — mensagem)
- src/main/java/br/org/edu/ifrn/LojaCarro/model/Carro.java:1 — MINOR — Rename this package name to match the regular expression '^[a-z_]+(\.[a-z_][a-z0-9_]*)*$'.
- src/main/java/br/org/edu/ifrn/LojaCarro/security/RateLimitingFilter.java:1 — MINOR — Rename this package name to match the regular expression.
- src/main/java/br/org/edu/ifrn/LojaCarro/security/RateLimitingFilter.java:26 — BLOCKER — "logger" is the name of a field in "GenericFilterBean".
- src/main/java/br/org/edu/ifrn/LojaCarro/security/RateLimitingFilter.java:42 — MINOR — Remove this use of "Bucket4j"; it is deprecated.
- src/main/java/br/org/edu/ifrn/LojaCarro/controllers/AuthController.java:23 — CRITICAL — Remove usage of generic wildcard type.
- src/main/java/br/org/edu/ifrn/LojaCarro/security/JwtAuthenticationFilter.java:22 — CRITICAL — Add a nested comment explaining why this method is empty, throw an UnsupportedOperationException or complete the implementation.
- src/main/java/br/org/edu/ifrn/LojaCarro/security/SecurityConfig.java: lines ~21-30 — MAJOR — Remove calls to deprecated methods.
- vários testes em src/test/... — INFO — Remove 'public' modifier from test methods/classes.

Prioridade recomendada:
1. CRITICAL / BLOCKER (corrigir primeiro): logger name conflict, generic wildcard, empty method in JwtAuthenticationFilter
2. MAJOR: deprecated API usage in SecurityConfig
3. MINOR/INFO: package naming, modifiers, deprecated builder usage

Observações:
- O arquivo gerado em `/tmp/sonar_issues.txt` contém o JSON resumido das issues; use-o como fonte canônica.
