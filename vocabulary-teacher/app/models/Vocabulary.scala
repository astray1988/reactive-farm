package models

import play.api.i18n.Lang

/**
 * Created by dylan on 2/13/16.
 */
case class Vocabulary(sourceLanguage: Lang, targetLanguage: Lang, word: String, translation: String)
