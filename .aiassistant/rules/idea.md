---
apply: always
---

# JetBrains IDEA

## Problem Inspection

Fix issues based on problems reported by IDEA (including warnings and issues related to comments and documentation)

## File Context

Prefer using IDEA MCP to determine which file the user is currently viewing

## Build/Run Project

When verifying code correctness through compilation, only compile the JVM target — compiling all targets is too slow

When running unit tests, only run JVM platform tests
