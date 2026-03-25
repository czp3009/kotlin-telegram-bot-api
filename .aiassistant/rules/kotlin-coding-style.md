---
apply: by file patterns
patterns: *.kt,*.kts
---

# Kotlin Coding Style

Use official code style

## Comments

Do not use numbered lists in comments

## KDoc

Do not write KDoc unless explicitly asked. When asked to modify KDoc, only modify existing KDoc, do not add new KDoc

After modifying code, check whether the associated KDoc and related KDoc would become inconsistent with the code
behavior due to this modification. If incorrect, make corrections.

Do not include import statements in example code within KDoc.
