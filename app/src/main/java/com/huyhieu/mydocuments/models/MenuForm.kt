package com.huyhieu.mydocuments.models

data class MenuForm(var name: String, var onNavigate: (() -> Unit)? = null)