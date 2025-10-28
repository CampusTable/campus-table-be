package com.campustable.be.domain.auth.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class ScrapingStructureChangedException extends CustomException {

  public ScrapingStructureChangedException() {
    super(ErrorCode.SCRAPING_STRUCTURE_CHANGED);
  }
}
