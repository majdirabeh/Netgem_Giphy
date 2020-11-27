package fr.dev.majdi.netgem.giphy.model

data class ListGifModel(
    val data: List<Data>,
    val pagination: pagination,
    val meta: Meta
)