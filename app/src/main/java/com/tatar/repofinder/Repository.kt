package com.tatar.repofinder

data class Repository(
    var name: String,
    var description: String,
    var forkCount: Int,
    var ownerName: String,
    var ownerAvatarUrl: String,
    var primaryLanguage: String
)
