package hu.ait.shoppingcart.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface groupIdDAO {


    @Query("SELECT * FROM groupId")
    fun getAllGroups(): List<groupId>


    @Insert
    fun insertGroup(groupid: groupId): Long

    @Delete
    fun deleteGroup(groupid: groupId)

}