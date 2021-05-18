package com.example.kotlinstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

/*
* Практическое задание
*
* Александр Аникин, [13.05.21 11:39]
продумай функционал приложения. мы будем писать его вместе по курсу, но весь дополнительный функционал и доработки - на тебе. потому что мы будем имплементировать минимум только для усвоения материала

Александр Аникин, [13.05.21 11:40]
плюс потренируйся в элементарных вещах на Котлине. Нарисуй простенький UI типа двух кнопок и счетчика

Александр Аникин, [13.05.21 11:40]
повесь листенеры на них

Александр Аникин, [13.05.21 11:41]
подключи синтетик
*
Прежде чем приступать к первому практическому заданию, вам нужно определиться с курсовым
проектом. Если всё ещё чувствуете себя неуверенно в коде, то можно пойти по более лёгкому пути и
писать своё приложение «Погода» на основе примеров из урока. Или взяться за совершенно новый
проект «Поиск по фильмам», для которого даются практические задания и который потребует от вас
большей самостоятельности и вовлечённости.
Что это будет?
1. Погодное приложение, как в примерах на занятиях, но с вашими улучшениями или
дополнениями.
2. Приложение для поиска фильмов, по которому вам будут даваться все практические задания.
Выбор за вами!

* Изучить API погоды Яндекса, посмотреть примеры и зарегистрироваться в качестве
разработчика, получить свой ключ разработчика.
7. Изучить API базы данных с фильмами и зарегистрироваться в качестве разработчика,
получить свой ключ разработчика.
8. Определиться с экранами и функционалом вашего будущего приложения с фильмами на
основе возможностей API.
* */

internal class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, PersonFragment())
            .commitAllowingStateLoss()
    }

    override fun passDataComm(person: Person) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, FragmentGreeting().apply {
                arguments = bundleOf(PersonFragment.PERSON_KEY to person)
            })
            .addToBackStack(null).commitAllowingStateLoss()
    }
}
