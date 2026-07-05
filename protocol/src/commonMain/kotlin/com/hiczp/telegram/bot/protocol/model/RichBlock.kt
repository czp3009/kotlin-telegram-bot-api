// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object represents a block in a rich formatted message. Currently, it can be any of the following types:
 * RichBlockParagraph RichBlockSectionHeading RichBlockPreformatted RichBlockFooter RichBlockDivider RichBlockMathematicalExpression RichBlockAnchor RichBlockList RichBlockBlockQuotation RichBlockPullQuotation RichBlockCollage RichBlockSlideshow RichBlockTable RichBlockDetails RichBlockMap RichBlockAnimation RichBlockAudio RichBlockPhoto RichBlockVideo RichBlockVoiceNote RichBlockThinking
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface RichBlock

/**
 * A text paragraph, corresponding to the HTML tag <p>.
 */
@Serializable
@SerialName("paragraph")
public data class RichBlockParagraph(
    /**
     * Text of the block
     */
    public val text: RichText,
) : RichBlock

/**
 * A section heading, corresponding to the HTML tags <h1>, <h2>, <h3>, <h4>, <h5>, or <h6>.
 */
@Serializable
@SerialName("heading")
public data class RichBlockSectionHeading(
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * Relative size of the text font; 1-6, 1 is the largest, 6 is the smallest
     */
    public val size: Long,
) : RichBlock

/**
 * A preformatted text block, corresponding to the nested HTML tags <pre> and <code>.
 */
@Serializable
@SerialName("pre")
public data class RichBlockPreformatted(
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * *Optional*. The programming language of the text
     */
    public val language: String? = null,
) : RichBlock

/**
 * A footer, corresponding to the HTML tag <footer>.
 */
@Serializable
@SerialName("footer")
public data class RichBlockFooter(
    /**
     * Text of the block
     */
    public val text: RichText,
) : RichBlock

/**
 * A divider, corresponding to the HTML tag <hr/>.
 */
@Serializable
@SerialName("divider")
public class RichBlockDivider : RichBlock

/**
 * A block with a mathematical expression in LaTeX format, corresponding to the custom HTML tag <tg-math-block>.
 */
@Serializable
@SerialName("mathematical_expression")
public data class RichBlockMathematicalExpression(
    /**
     * The mathematical expression in LaTeX format
     */
    public val expression: String,
) : RichBlock

/**
 * A block with an anchor, corresponding to the HTML tag <a> with the attribute name.
 */
@Serializable
@SerialName("anchor")
public data class RichBlockAnchor(
    /**
     * The name of the anchor
     */
    public val name: String,
) : RichBlock

/**
 * A list of blocks, corresponding to the HTML tag <ul> or <ol> with multiple nested tags <li>.
 */
@Serializable
@SerialName("list")
public data class RichBlockList(
    /**
     * Items of the list
     */
    public val items: List<RichBlockListItem>,
) : RichBlock

/**
 * A block quotation, corresponding to the HTML tag <blockquote>.
 */
@Serializable
@SerialName("blockquote")
public data class RichBlockBlockQuotation(
    /**
     * Content of the block
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Credit of the block
     */
    public val credit: RichText? = null,
) : RichBlock

/**
 * A quotation with centered text, loosely corresponding to the HTML tag <aside>.
 */
@Serializable
@SerialName("pullquote")
public data class RichBlockPullQuotation(
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * *Optional*. Credit of the block
     */
    public val credit: RichText? = null,
) : RichBlock

/**
 * A collage, corresponding to the custom HTML tag <tg-collage>.
 */
@Serializable
@SerialName("collage")
public data class RichBlockCollage(
    /**
     * Elements of the collage
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A slideshow, corresponding to the custom HTML tag <tg-slideshow>.
 */
@Serializable
@SerialName("slideshow")
public data class RichBlockSlideshow(
    /**
     * Elements of the slideshow
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A table, corresponding to the HTML tag <table>.
 */
@Serializable
@SerialName("table")
public data class RichBlockTable(
    /**
     * Cells of the table
     */
    public val cells: List<List<RichBlockTableCell>>,
    /**
     * *Optional*. *True*, if the table has borders
     */
    @SerialName("is_bordered")
    public val isBordered: Boolean? = null,
    /**
     * *Optional*. *True*, if the table is striped
     */
    @SerialName("is_striped")
    public val isStriped: Boolean? = null,
    /**
     * *Optional*. Caption of the table
     */
    public val caption: RichText? = null,
) : RichBlock

/**
 * An expandable block for details disclosure, corresponding to the HTML tag <details>.
 */
@Serializable
@SerialName("details")
public data class RichBlockDetails(
    /**
     * Always shown summary of the block
     */
    public val summary: RichText,
    /**
     * Content of the block
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the content of the block is visible by default
     */
    @SerialName("is_open")
    public val isOpen: Boolean? = null,
) : RichBlock

/**
 * A block with a map, corresponding to the custom HTML tag <tg-map>.
 */
@Serializable
@SerialName("map")
public data class RichBlockMap(
    /**
     * Location of the center of the map
     */
    public val location: Location,
    /**
     * Map zoom level; 13-20
     */
    public val zoom: Long,
    /**
     * Expected width of the map
     */
    public val width: Long,
    /**
     * Expected height of the map
     */
    public val height: Long,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with an animation, corresponding to the HTML tag <video>.
 */
@Serializable
@SerialName("animation")
public data class RichBlockAnimation(
    /**
     * The animation
     */
    public val animation: Animation,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with a music file, corresponding to the HTML tag <audio>.
 */
@Serializable
@SerialName("audio")
public data class RichBlockAudio(
    /**
     * The audio
     */
    public val audio: Audio,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with a photo, corresponding to the HTML tag <img>.
 */
@Serializable
@SerialName("photo")
public data class RichBlockPhoto(
    /**
     * Available sizes of the photo
     */
    public val photo: List<PhotoSize>,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with a video, corresponding to the HTML tag <video>.
 */
@Serializable
@SerialName("video")
public data class RichBlockVideo(
    /**
     * The video
     */
    public val video: Video,
    /**
     * *Optional*. *True*, if the media preview is covered by a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with a voice note, corresponding to the HTML tag <audio>.
 */
@Serializable
@SerialName("voice_note")
public data class RichBlockVoiceNote(
    /**
     * The voice note
     */
    @SerialName("voice_note")
    public val voiceNote: Voice,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock

/**
 * A block with a “Thinking…” placeholder, corresponding to the custom HTML tag <tg-thinking>. The block may be used only in sendRichMessageDraft, therefore it can't be received in messages. See https://t.me/addemoji/AIActions for examples of custom emoji, which are recommended for usage in the block.
 */
@Serializable
@SerialName("thinking")
public data class RichBlockThinking(
    /**
     * Text of the block. See https://t.me/addemoji/AIActions for examples of custom emoji, which are recommended for usage in the block.
     */
    public val text: RichText,
) : RichBlock
