package services

import com.google.inject.Singleton
import models.Vocabulary
import play.api.i18n.Lang

import scala.util.Random

/**
 * Created by dylan on 2/13/16.
 */

@Singleton
class VocabularService {
  private var allVocabulary = List(
    Vocabulary(Lang("en"), Lang("fr"), "hello", "bonjour"),
    Vocabulary(Lang("en"), Lang("fr"), "play", "jouer")
  )

  def addVocabulary(v: Vocabulary): Boolean = {
    if (!allVocabulary.contains(v)) {
      allVocabulary = v :: allVocabulary
      true
    } else {
      false
    }
  }

  def findRandomVocabulary (sourceLang: Lang, targetLang: Lang): Option[Vocabulary] = {
    Random.shuffle(allVocabulary.filter { v =>
      v.sourceLanguage == sourceLang &&
      v.targetLanguage == targetLang
    }).headOption
  }

  def verify(sourceLang: Lang, targetLang: Lang, word: String, translation: String): Boolean = {
    allVocabulary.contains(Vocabulary(sourceLang, targetLang, word, translation))
  }

}
