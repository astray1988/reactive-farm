package controllers

import play.api.i18n.Lang
import play.api.mvc.Controller


/**
  * Created by dylan on 2/13/16.
  */
class Quiz extends Controller {
  def quizz(sourceLang: Lang, targetLang: Lang) = TODO

  def check(sourceLang: Lang, targetLang: Lang, word: String, translations: String) = TODO

}
