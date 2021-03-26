package com.justordercompany.client.logic.models

import com.justordercompany.client.R

data class ModelSlideData
    (
        var title: String,
        val text: String?,
        val img_res_id: Int)
{
    companion object
    {
        fun getSlidesData(): ArrayList<ModelSlideData>
        {
            val slide_1 = ModelSlideData(
                "Добро пожаловать в JOC",
                null,
                R.drawable.img_intro1)

            val slide_2 = ModelSlideData(
                "Удобно",
                "Выберете подходящее заведение в списке или на карте. Используйте фильтр по рейтингу и стоимости для подбора лучшего варианта.",
                R.drawable.img_intro2)

            val slide_3 = ModelSlideData(
                "Быстро",
                "Делайте заказ к определённому времени или прямо сейчас, забудьте об очередях и трате времени.",
                R.drawable.img_intro3)

            val slide_4 = ModelSlideData(
                "Отзывы",
                "Оценивайте заказы, делитесь впечатлениями о заведении.",
                R.drawable.img_intro4)

            val slide_5 = ModelSlideData(
                "Безопасно",
                "Сервис позволяет минимизировать время в  местах распространения инфекции.",
                R.drawable.img_intro5)

            val slides = arrayListOf(slide_1, slide_2, slide_3, slide_4, slide_5)

            return slides
        }
    }
}