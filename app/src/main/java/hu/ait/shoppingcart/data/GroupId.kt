package hu.ait.shoppingcart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "groupId")
data class groupId(@PrimaryKey(autoGenerate = true) var id: Long?,
                 @ColumnInfo(name = "groupId") var groupId: String
)
