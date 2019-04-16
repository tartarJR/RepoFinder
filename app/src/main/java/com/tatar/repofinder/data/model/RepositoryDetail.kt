package com.tatar.repofinder.data.model

data class RepositoryDetail(
    var name: String,
    var subscribers: ArrayList<Subscriber>,
    var numberOFSubscribers: Int
)