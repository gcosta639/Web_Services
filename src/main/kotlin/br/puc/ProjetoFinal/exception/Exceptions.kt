package br.puc.ProjetoFinal.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)