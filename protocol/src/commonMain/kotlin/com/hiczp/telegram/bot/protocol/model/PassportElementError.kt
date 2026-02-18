// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object represents an error in the Telegram Passport element which was submitted that should be resolved by the user. It should be one of:
 * PassportElementErrorDataField PassportElementErrorFrontSide PassportElementErrorReverseSide PassportElementErrorSelfie PassportElementErrorFile PassportElementErrorFiles PassportElementErrorTranslationFile PassportElementErrorTranslationFiles PassportElementErrorUnspecified
 */
@Serializable
@JsonClassDiscriminator("source")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface PassportElementError

/**
 * Represents an issue in one of the data fields that was provided by the user. The error is considered resolved when the field's value changes.
 */
@Serializable
@SerialName("data")
public data class PassportElementErrorDataField(
    /**
     * The section of the user's Telegram Passport which has the error, one of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”, “address”
     */
    public val type: String,
    /**
     * Name of the data field which has the error
     */
    @SerialName("field_name")
    public val fieldName: String,
    /**
     * Base64-encoded data hash
     */
    @SerialName("data_hash")
    public val dataHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with the front side of a document. The error is considered resolved when the file with the front side of the document changes.
 */
@Serializable
@SerialName("front_side")
public data class PassportElementErrorFrontSide(
    /**
     * The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”
     */
    public val type: String,
    /**
     * Base64-encoded hash of the file with the front side of the document
     */
    @SerialName("file_hash")
    public val fileHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with the reverse side of a document. The error is considered resolved when the file with reverse side of the document changes.
 */
@Serializable
@SerialName("reverse_side")
public data class PassportElementErrorReverseSide(
    /**
     * The section of the user's Telegram Passport which has the issue, one of “driver_license”, “identity_card”
     */
    public val type: String,
    /**
     * Base64-encoded hash of the file with the reverse side of the document
     */
    @SerialName("file_hash")
    public val fileHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with the selfie with a document. The error is considered resolved when the file with the selfie changes.
 */
@Serializable
@SerialName("selfie")
public data class PassportElementErrorSelfie(
    /**
     * The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”
     */
    public val type: String,
    /**
     * Base64-encoded hash of the file with the selfie
     */
    @SerialName("file_hash")
    public val fileHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with a document scan. The error is considered resolved when the file with the document scan changes.
 */
@Serializable
@SerialName("file")
public data class PassportElementErrorFile(
    /**
     * The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
     */
    public val type: String,
    /**
     * Base64-encoded file hash
     */
    @SerialName("file_hash")
    public val fileHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with a list of scans. The error is considered resolved when the list of files containing the scans changes.
 */
@Serializable
@SerialName("files")
public data class PassportElementErrorFiles(
    /**
     * The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
     */
    public val type: String,
    /**
     * List of base64-encoded file hashes
     */
    @SerialName("file_hashes")
    public val fileHashes: List<String>,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with one of the files that constitute the translation of a document. The error is considered resolved when the file changes.
 */
@Serializable
@SerialName("translation_file")
public data class PassportElementErrorTranslationFile(
    /**
     * Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
     */
    public val type: String,
    /**
     * Base64-encoded file hash
     */
    @SerialName("file_hash")
    public val fileHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue with the translated version of a document. The error is considered resolved when a file with the document translation change.
 */
@Serializable
@SerialName("translation_files")
public data class PassportElementErrorTranslationFiles(
    /**
     * Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
     */
    public val type: String,
    /**
     * List of base64-encoded file hashes
     */
    @SerialName("file_hashes")
    public val fileHashes: List<String>,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError

/**
 * Represents an issue in an unspecified place. The error is considered resolved when new data is added.
 */
@Serializable
@SerialName("unspecified")
public data class PassportElementErrorUnspecified(
    /**
     * Type of element of the user's Telegram Passport which has the issue
     */
    public val type: String,
    /**
     * Base64-encoded element hash
     */
    @SerialName("element_hash")
    public val elementHash: String,
    /**
     * Error message
     */
    public val message: String,
) : PassportElementError
