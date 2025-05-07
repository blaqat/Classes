import sanitizeHTML from 'sanitize-html';

// Test Function
export const sum = (a, b) => a + b;

/**
  Valid book titles in this situation can include:
    - Cannot be any form of "Boaty McBoatface", case insensitive
    - English alphabet characters
    - Arabic numerals
    - Spaces, but no other whitespace like tabs or newlines
    - Quotes, both single and double
    - Hyphens
    - No leading or trailing whitespace
    - No newlines or tabs
*/
export function isTitle(str) {
  let normStr = str.normalize('NFC');

  // Constants
  const blacklistedTitles = ["boaty mcboatface"];
  const validCharacters = /^[A-Za-z\d '"\-\p{Letter}]+$/u;

  // Validation - These return True if passing
  const isNotBlacklisted = (str) => !blacklistedTitles.includes(str.toLowerCase());
  const hasOnlyValidCharacters = (str) => validCharacters.test(str);
  const hasNoSurroundingWhitespace = (str) => !(str.startsWith(" ") || str.endsWith(" "));
  const hasNoReturnsOrTabs = (str) => !str.split('').some(c => c === "\n" || c === "\t");

  let validations = [isNotBlacklisted, hasNoSurroundingWhitespace, hasOnlyValidCharacters, hasNoReturnsOrTabs];
  return validations.every(check => check(normStr));
}

/**
  Are the two titles *effectively* the same when searching?

  This function will be used as part of a search feature, so it should be
  flexible when dealing with diacritics and ligatures.

  Input: two raw strings
  Output: true if they are "similar enough" to each other

  We define two strings as the "similar enough" as:

    * ignore leading and trailing whitespace
    * same sequence of "letters", ignoring diacritics and ligatures, that is:
      anything that is NOT a letter in the UTF-8 decomposed form is removed
    * Ligature "\u00E6" or æ is equivalent to "ae"
    * German character "\u1E9E" or ẞ is equivalent to "ss"
*/
export function isSameTitle(str1, str2) {
  // Constants
  const characterMap = {
    "\u00E6": "ae",
    "\u1E9E": "ss",
    "\uFB00": "ff"
  };

  // Check that both inputs are actually strings
  if (str1 instanceof String) str1 = str1.valueOf();
  if (str2 instanceof String) str2 = str2.valueOf();
  if (typeof str1 !== 'string' || typeof str2 !== 'string') return false;

  // Reutrn early if they are the same
  if (str1 == str2) return true;

  // Normalize the strings
  const normalizeString = (str) => {
    str = str.normalize('NFD').trim();
    return str.split('')
      // Remove non UTF-8 characters
      .filter(c => c.match(/\p{Letter}/u))
      // Map mappable characters
      .map(c => characterMap[c] ?? c)
      .join('');
  }

  let normStr1 = normalizeString(str1);
  let normStr2 = normalizeString(str2);

  return normStr1 === normStr2;
}

/**
  Page range string.

  Count, inclusively, the number of pages mentioned in the string.

  This is modeled after the string you can use to specify page ranges in
  books, or in a print dialog.

  Example page ranges, copied from our test cases:
    1          ===> 1 page
    p3         ===> 1 page
    1-2        ===> 2 pages
    10-100     ===> 91 pages
    1-3,5-6,9  ===> 6 pages
    1-3,5-6,p9 ===> 6 pages

  A range that goes DOWN still counts, but is never negative.

  Whitespace is allowed anywhere in the string with no effect.

  If the string is over 1000 characters, return undefined
  If the string returns in NaN, return undefined
  If the string does not properly fit the format, return 0
*/
export function countPages(rawStr) {
  rawStr = rawStr.replaceAll(" ", "")
  const validPageFormat = /^(((p?\d+-p?\d+)|(p?\d+)),?)+$/;
  const pageCaptureRegex = /(?<range>(?<from>p?\d+)-(?<to>p?\d+))|(?<page>p?\d+)/g;

  // Return early if not in correct format
  if (!validPageFormat.test(rawStr)) return 0;

  let pages = 0;
  const matches = rawStr.matchAll(pageCaptureRegex);

  for (const match of matches) {
    // Check Single Page
    if (match.groups.page && cleanPageNum(match.groups.page)) pages++;
    // Check Page Range
    else if (match.groups.range) {
      let fromPage = cleanPageNum(match.groups.from);
      let toPage = cleanPageNum(match.groups.to);
      if (!fromPage || !toPage) return undefined;
      if (!Number.isSafeInteger(fromPage) || !Number.isSafeInteger(toPage)) {
        return undefined;
      }
      pages += Math.abs(toPage - fromPage) + 1;
    }
  }

  return pages
}

/**
  Perform a best-effort cleansing of the page number.
  Given: a raw string
  Returns: an integer, ignoring leading and trailing whitespace. And it can have p in front of it.
*/
export function cleanPageNum(str) {
  let pageMatches = str.trim().match(/^p?(?<page>\d+)$/);
  if (!pageMatches?.groups?.page) return undefined
  return parseInt(pageMatches.groups.page);
}

/**
  Given a string, return another string that is safe for embedding into HTML.
    * Use the sanitize-html library: https://www.npmjs.com/package/sanitize-html
    * Configure it to *only* allow <b> tags and <i> tags
      (Read the README to learn how to do this)
*/
export function cleanForHTML(dirty) {
  let sanitized = sanitizeHTML(dirty, {
    allowedTags: ['b', 'i'],
    disallowedTagsMode: 'escape'
  });

  let sanitizedQuotesEscaped = sanitized.replaceAll('"', "&quot;").replaceAll("'", "&#x27;");

  return sanitizedQuotesEscaped
}

// To* all my JS nitpickers...
// We are using CommonJ- About that... :p
export default {
  sum,
  isTitle,
  countPages,
  cleanPageNum,
  isSameTitle,
  cleanForHTML
};
