package com.justordercompany.client.logic.utils

import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.utils.strings.StringManager

class ValidationManager
{
    companion object
    {
        private fun getPleaseFillStr(field:String):String
        {
            return getStringMy(R.string.please_fill,field)
        }

        fun validateCodeSend(code:String?,phone:String?,is_checked:Boolean?):Boolean
        {
            if (code == null)
            {
                return false
            }

            if (phone == null)
            {
                return false
            }

            if(is_checked == null || is_checked == false)
            {
                return false
            }

            return true
        }

        fun getCodeSendErrors(code:String?,phone:String?,is_checked:Boolean?):String
        {
            val errors: ArrayList<String> = arrayListOf()

            if(code == null)
            {
                errors.add(getPleaseFillStr(getStringMy(R.string.code)))
            }
            
            if(phone == null)
            {
                errors.add("**** Internal error no phone ****")
            }
            
            if(is_checked == null || is_checked == false)
            {
                errors.add(getStringMy(R.string.please_confirm_offert))
            }

            return StringManager.listOfStringToSingle(errors)
        }
    }
}