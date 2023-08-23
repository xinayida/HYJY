package com.futuretech.base.demo.realm

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class Item() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var isComplete: Boolean = false
    var summary: String = ""
    var owner_id: String = ""

    constructor(ownerId: String = "") : this() {
        owner_id = ownerId
    }
}

class Task : RealmObject {
    @PrimaryKey
    var _id: RealmUUID = RealmUUID.random()
    var name: String = ""
    var status: String = ""

}

class Frog : RealmObject {
    var name: String = ""
    var favoriteSnacks: RealmSet<Snack> = realmSetOf()
}

class Snack : RealmObject {
    var name: String? = null
}


class RealDemo {
    val config = RealmConfiguration.create(schema = setOf(Item::class))
    val realm: Realm = Realm.open(config)

    // all items in the realm
    val items: RealmResults<Item> = realm.query<Item>().find()

    // items in the realm whose name begins with the letter 'D'
    val itemsThatBeginWIthD: RealmResults<Item> =
        realm.query<Item>("summary BEGINSWITH $0", "D")
            .find()

    //  todo items that have not been completed yet
    val incompleteItems: RealmResults<Item> =
        realm.query<Item>("isComplete == false")
            .find()

    fun write() {
        CoroutineScope(Dispatchers.Main).launch {
            realm.write {

            }
        }
        realm.writeBlocking {
            copyToRealm(Item().apply {
                summary = "Do the laundry"
                isComplete = false
            })
        }

        // change the first item with open status to complete to show that the todo item has been done
        realm.writeBlocking {
            findLatest(incompleteItems[0])?.isComplete = true
        }
    }

    fun delete() {
        // delete the first item in the realm
        realm.writeBlocking {
            val writeTransactionItems = query<Item>().find()
            delete(writeTransactionItems.first())
        }
    }

    fun watch() {
        // flow.collect() is blocking -- run it in a background context
        val job = CoroutineScope(Dispatchers.Default).launch {
            // create a Flow from the Item collection, then add a listener to the Flow
            val itemsFlow = items.asFlow()
            itemsFlow.collect { changes: ResultsChange<Item> ->
                when (changes) {
                    // UpdatedResults means this change represents an update/insert/delete operation
                    is UpdatedResults -> {
                        changes.insertions // indexes of inserted objects
                        changes.insertionRanges // ranges of inserted objects
                        changes.changes // indexes of modified objects
                        changes.changeRanges // ranges of modified objects
                        changes.deletions // indexes of deleted objects
                        changes.deletionRanges // ranges of deleted objects
                        changes.list // the full collection of objects
                    }

                    else -> {
                        // types other than UpdatedResults are not changes -- ignore them
                    }
                }
            }
        }

        job.cancel() // cancel the coroutine containing the listener
    }

    fun demo() {
        val config = RealmConfiguration.Builder(schema = setOf(Task::class))
            .build()
        val realm = Realm.open(config)
        // fetch objects from a realm as Flowables
        CoroutineScope(Dispatchers.Main).launch {
            val flow: Flow<ResultsChange<Task>> = realm.query<Task>().asFlow()
            flow.collect { task ->
                Log.v("RealDemo", "Task: $task")
            }
        }
        // write an object to the realm in a coroutine
        CoroutineScope(Dispatchers.Main).launch {
            realm.write {
                copyToRealm(Task().apply { name = "my task"; status = "Open" })
            }
        }
    }
}