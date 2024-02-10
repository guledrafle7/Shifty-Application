package com.example.shifty.firebase

import com.example.shifty.model.*
import com.example.shifty.utils.Constant.Companion.EMAIL_KEY
import com.example.shifty.utils.Constant.Companion.EMPLOYEE
import com.example.shifty.utils.Constant.Companion.currentDate
import com.example.shifty.utils.Constant.Companion.currentUser
import com.example.shifty.utils.Constant.Companion.date
import com.example.shifty.utils.Constant.Companion.editor
import com.example.shifty.utils.showLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataBaseOperations {
     companion object {
          private var database = FirebaseFirestore.getInstance()
          private var auth = FirebaseAuth.getInstance()

          private const val DB_USERS: String = "Users"
          private const val DB_QR: String = "QR"
          private const val DB_SHIFT: String = "Shift"
          private const val DB_TIME_OFF: String = "TimeOff"
          private const val DB_PAY: String = "Pay"

          fun authenticate(email: String, password: String, resultListener: (Boolean, String, Users?) -> Unit) {
               auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful) {
                         val uid = auth.currentUser!!.uid
                         loadUserData(uid, resultListener)
                    } else {
                         resultListener(true,"Email or password is incorrect",null)
                    }
               }
          }

          fun createUser(user: Users,password: String,resultListener: (Boolean,String?) -> Unit) {
               auth.createUserWithEmailAndPassword(user.email, password)
                    .addOnCompleteListener {
                         if (it.isSuccessful) {
                              val uid = auth.currentUser!!.uid
                              user.uid=uid

                              insertUserData(user,resultListener)
                         } else {
                              resultListener(true, it.exception!!.message)
                         }

                    }
          }

          private fun insertUserData(user: Users, resultListener: (Boolean, String?) -> Unit) {
               database.collection(DB_USERS).document(user.uid).set(user).addOnCompleteListener {
                    if(it.isSuccessful) {
                         resultListener(false,"Success")
                    } else {
                         resultListener(true,"Failed to insert user data")
                    }
               }
          }

           fun saveQrCode(qrCode: QrCode,resultListener: (Boolean,String) -> Unit) {
               database.collection(DB_QR).document("$date${qrCode.uid}").set(qrCode).addOnCompleteListener {
                    if(it.isSuccessful) {
                         resultListener(false,"Success")
                    } else {
                         resultListener(true,"Failed to save qr code")
                    }
               }
          }

          fun saveShifts(shift: Shift,resultListener: (Boolean,String) -> Unit) {
               database.collection(DB_SHIFT).document(shift.uid).set(shift).addOnCompleteListener {
                    if(it.isSuccessful) {
                         resultListener(true,"Successfully shifted")
                    } else {
                         resultListener(false,"Failed to save Shift")
                    }
               }
          }

          fun saveTimeOff(timeOff: TimeOff,resultListener: (Boolean,String) -> Unit) {
               database.collection(DB_TIME_OFF).document(timeOff.uid).set(timeOff).addOnCompleteListener {
                    if(it.isSuccessful) {
                         resultListener(true,"Successfully TimeOff")
                    } else {
                         resultListener(false,"Failed to save TimeOff")
                    }
               }
          }

          fun updateTimeOff(timeOff: TimeOff,action: String,resultListener: (Boolean) -> Unit) {
               val updates = hashMapOf<String, Any>(
                    "allowed" to action
               )
               database.collection(DB_TIME_OFF).document(timeOff.uid).update(updates)
                    .addOnSuccessListener {
                         resultListener(true)
                    }
                    .addOnFailureListener {
                         resultListener(false)
                    }
          }

          fun savePays(pay: Pay,resultListener: (Boolean,String) -> Unit) {
               database.collection(DB_PAY).document(pay.uid).set(pay).addOnCompleteListener {
                    if(it.isSuccessful) {
                         resultListener(true,"Successfully Paid")
                    } else {
                         resultListener(false,"Failed to save pay")
                    }
               }
          }

           fun loadUserData(uid:String,resultListener: (Boolean, String, Users?) -> Unit) {
               database.collection(DB_USERS).document(uid).get().addOnCompleteListener {
                    if(it.isSuccessful && it.result.exists()) {
                         val user=it.result.toObject(Users::class.java)
                         resultListener(false,"Success",user)
                    } else {
                         resultListener(true,"User data not found",null)
                    }
               }
          }

          fun getAllUser(resultListener: (ArrayList<Users>?) -> Unit) {
               val list: ArrayList<Users> = ArrayList()
               database.collection(DB_USERS).get()
                    .addOnSuccessListener { documents ->
                         if(!documents.isEmpty) {
                              for (document in documents) {
                                   val data = document.data

                                   val userId = data["uid"].toString()
                                   val email = data["email"].toString()
                                   val role = data["role"].toString().toInt()

                                   if(role == EMPLOYEE) {
                                        val user = Users(uid = userId, email = email, role = role)
                                        list.add(user)
                                   }
                              }
                              resultListener(list)
                         } else
                              resultListener(null)
                    }
                    .addOnFailureListener {
                         showLog(it.message.toString())
                    }
          }

          fun getAllEmployees(resultListener: (ArrayList<TimeOff>?) -> Unit){
               val list: ArrayList<TimeOff> = ArrayList()
               database.collection(DB_TIME_OFF).get()
                    .addOnSuccessListener { documents ->
                         if(!documents.isEmpty) {
                              for (document in documents) {
                                   val data = document.data

                                   val uid = data["uid"].toString()
                                   val email = data["email"].toString()
                                   val endDate = data["endDate"].toString()
                                   val startDate = data["startDate"].toString()
                                   val isAllowed = data["allowed"].toString()

                                   if(isAllowed == "-1" || (startDate.toInt() <= date.toInt() && endDate.toInt() <= date.toInt())) {
                                        val timeOff = TimeOff(
                                             uid = uid,
                                             email = email,
                                             startDate = startDate,
                                             endDate = endDate,
                                             isAllowed = isAllowed
                                        )
                                        list.add(timeOff)
                                   }
                              }
                              resultListener(list)
                         } else
                              resultListener(null)
                    }
                    .addOnFailureListener {
                         showLog(it.message.toString())
                    }
          }

          fun getQrCodes(date: String,resultListener: (ArrayList<QrCode>?) -> Unit) {
               val list: ArrayList<QrCode> = ArrayList()
//               database.collection(DB_QR).get()
//                    .addOnSuccessListener { documents ->
//                         if(!documents.isEmpty) {
//                              for (document in documents) {
//                                   val data = document.data
//                                   val uid = data["uid"].toString()
//                                   val email = data["email"].toString()
//                                   val shift = data["shift"].toString()
//                                   val date = data["date"].toString()
//                                   val checkIn = data["checkIn"].toString()
//                                   val checkOut = data["checkOut"].toString()
//
//                                   val qrCode = QrCode(uid = uid, email = email, shift = shift, date = date, checkIn = checkIn, checkOut = checkOut)
//                                   list.add(qrCode)
//                              }
//                              resultListener(list)
//                         } else
//                              resultListener(null)
//                    }
//                    .addOnFailureListener {
//                         showLog(it.message.toString())
//                    }
               list.clear()
               database.collection(DB_QR).whereEqualTo("date", date).get()
                    .addOnSuccessListener { documents ->
                         if(!documents.isEmpty) {
                              for (document in documents) {
                                   val data = document.data
                                   val uid = data["uid"].toString()
                                   val email = data["email"].toString()
                                   val shift = data["shift"].toString()
                                   val rDate = data["date"].toString()
                                   val checkIn = data["checkIn"].toString()
                                   val checkOut = data["checkOut"].toString()

                                   val qrCode = QrCode(uid = uid, email = email, shift = shift, date = rDate, checkIn = checkIn, checkOut = checkOut)
                                   list.add(qrCode)
                              }
                              resultListener(list)
                         } else {
                              resultListener(null)
                         }
                    }
                    .addOnFailureListener {
                         showLog(it.message.toString())
                    }
          }

          fun getAllTimeOffRequest(resultListener: (ArrayList<TimeOff>?) -> Unit) {
               val list: ArrayList<TimeOff> = ArrayList()
               database.collection(DB_TIME_OFF).get()
                    .addOnSuccessListener { documents ->
                         if(!documents.isEmpty) {
                              for (document in documents) {
                                   val data = document.data

                                   val uid = data["uid"].toString()
                                   val email = data["email"].toString()
                                   val endDate = data["endDate"].toString()
                                   val startDate = data["startDate"].toString()
                                   val isAllowed = data["allowed"].toString()

                                   if(isAllowed == "0") {
                                        val timeOff = TimeOff(
                                             uid = uid,
                                             startDate = startDate,
                                             endDate = endDate,
                                             isAllowed = isAllowed,
                                             email = email
                                        )
                                        list.add(timeOff)
                                   }
                              }
                              resultListener(list)
                         } else
                              resultListener(null)
                    }
                    .addOnFailureListener {
                         showLog(it.message.toString())
                    }
          }

          fun getEmployeeShift(uid: String,resultListener: (Shift?) -> Unit) {
               database.collection(DB_SHIFT).whereEqualTo("uid", uid).get()
                    .addOnSuccessListener { querySnapshot ->
                         if (!querySnapshot.isEmpty) {
                              val documentSnapshot = querySnapshot.documents[0]
                              val userid = documentSnapshot.getString("uid").toString()
                              val date = documentSnapshot.getString("date").toString()
                              val timeSlot = documentSnapshot.getString("timeSlot").toString()

                              val shift = Shift(uid = userid, timeSlot = timeSlot, date = date)
                              resultListener(shift)
                              showLog(querySnapshot)
                         } else {
                              resultListener(null)
                              showLog(querySnapshot)
                         }
                    }
                    .addOnFailureListener {
                         showLog(it)
                    }
          }

          fun getAllPaid(uid: String,resultListener: (ArrayList<Pay>?) -> Unit) {
               val list: ArrayList<Pay> = ArrayList()
               database.collection(DB_PAY).whereEqualTo("uid", uid).get()
                    .addOnSuccessListener { documents ->
                         if(!documents.isEmpty) {
                              for (document in documents) {
                                   val data = document.data

                                   val userId = data["uid"].toString()
                                   val date = data["date"].toString()
                                   val earn = data["earn"].toString()

                                   val pay = Pay(uid = userId, date = date, earn = earn)
                                   list.add(pay)
                              }
                              resultListener(list)
                         } else
                              resultListener(null)
                    }
                    .addOnFailureListener {
                         showLog(it.message.toString())
                    }
          }
     }
}