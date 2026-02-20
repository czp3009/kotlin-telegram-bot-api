package com.hiczp.telegram.bot.codegen

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class TelegramEventProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return TelegramEventProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}
