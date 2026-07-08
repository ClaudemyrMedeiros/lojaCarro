Sprint: Soluções propostas para issues SonarQube

Objetivo: aplicar correções automáticas quando seguras e documentar as alterações com prints (logs) das mudanças e motivos.

Plano de correções rápidas (automáticas):
- Package naming (MINOR): Renomear declarações de pacote de `br.org.edu.ifrn.LojaCarro` para `br.org.edu.ifrn.lojacarro` (todos os arquivos). Testar compilação.
- Test modifiers (INFO): Remover `public` em classes/metodos de teste JUnit5.
- Carro fields (já aplicado): garantir campos `private` (feito).

Correções que exigem revisão manual:
- `logger` field conflict (BLOCKER): renomear o logger local para `log` ou usar `private static final Logger LOG = LoggerFactory.getLogger(...)` e evitar conflito com superclass.
- `Bucket4j` / `builder` deprecated (MINOR): atualizar uso para API atual (consultar docs do Bucket4j) ou atualizar dependência; implementar solução compatível.
- Generic wildcard no `AuthController` (CRITICAL): substituir `List<?>`/`ResponseEntity<?>` por tipos concretos (ex.: `ResponseEntity<UserDto>`), ajustar DTOs.
- JwtAuthenticationFilter (CRITICAL): documentar método vazio (javadoc) ou implementar/lançar `UnsupportedOperationException` se for intencional.
- SecurityConfig (MAJOR): substituir chamadas de API Spring Security depreciadas por alternativas recomendadas (ex.: uso de `SecurityFilterChain` e `HttpSecurity` atualizados).

Ações automatizadas que vou executar agora (com commits separados):
1. Remover modifiers `public` em testes.
2. Renomear pacotes para lowercase (`lojacarro`) e atualizar `package` declarations.
3. Ajustar logger name no `RateLimitingFilter` para `LOG`.

Para as demais mudanças (dep. Bucket4j, SecurityConfig, AuthController generics, JwtAuthenticationFilter) vou gerar PRs com sugestões e mostrar prints das linhas alteradas e justificativas.

Depois de aplicar cada correção, vou rodar `mvn test` e `mvn sonar:sonar` novamente e incluir no log a lista de issues resolvidas e as que permaneceram.
