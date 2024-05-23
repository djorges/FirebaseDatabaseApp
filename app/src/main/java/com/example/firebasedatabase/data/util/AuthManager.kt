package com.example.firebasedatabase.data.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.firebasedatabase.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthManager(
    private val context: Context
){
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private val signInClient = Identity.getSignInClient(context)

    private val googleSignInClient: GoogleSignInClient by lazy{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try{
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user)
        }catch(e: Exception){
            AuthRes.Error(e.message ?: "Sign up failed.")
        }
    }

    suspend fun signInAnonymously(): AuthRes<FirebaseUser> {
        return try{
            val result = auth.signInAnonymously().await()
            AuthRes.Success(result.user!!)
        }catch(e: Exception){
            AuthRes.Error(e.message ?: "Sign in failed.")
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try{
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user)
        }catch(e: Exception) {
            AuthRes.Error(e.message ?: "Sign in failed.")
        }
    }

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try{
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        }catch(e: Exception) {
            AuthRes.Error(e.message ?: "Reset password failed.")
        }
    }

    fun signOut() {
        auth.signOut()
        signInClient.signOut()
    }




    suspend fun signInWitGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser>? {
        return try{
            val result = auth.signInWithCredential(credential).await()
            result.user?.let{
                AuthRes.Success(it)
            } ?: throw Exception("Google sign in failed.")
        }catch(e: Exception){
            AuthRes.Error(e.message ?: "Google sign in failed.")
        }
    }

    fun signInWithGoogle(activityLauncher: ActivityResultLauncher<Intent>){
        val intent = googleSignInClient.signInIntent
        activityLauncher.launch(intent)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>):AuthRes<GoogleSignInAccount>?{
        return try{
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        }catch(e: Exception){
            AuthRes.Error(e.message ?: "Google sign in failed.")
        }
    }
}

sealed class AuthRes<out T>{
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val message: String): AuthRes<Nothing>()
}