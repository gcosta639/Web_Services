package br.puc.ProjetoFinal.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

// Constraint customizada do Bean Validation
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StrongPasswordValidator::class])
annotation class StrongPassword(
    val message: String = "A senha deve ter de 8 a 72 caracteres, com pelo menos uma letra e um número",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class StrongPasswordValidator : ConstraintValidator<StrongPassword, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true // a obrigatoriedade fica com o @NotBlank
        // O limite de 72 existe porque o BCrypt só considera os 72 primeiros bytes
        return value.length in 8..72 &&
                value.any { it.isLetter() } &&
                value.any { it.isDigit() }
    }
}