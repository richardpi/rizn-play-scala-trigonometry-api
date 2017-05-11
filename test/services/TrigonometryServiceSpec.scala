package servicestest

import org.scalatest.FunSuite
import services.TrigonometryService

class TrigonometryServiceSpec extends FunSuite {

  val trigonometryService = new TrigonometryService

  test("assert ASA") {
    val mapASA = Map("angleA" -> Some(75.0), "angleB" -> Some(60.0), "sideC" -> Some(340.0))
    val res = trigonometryService.calculate(mapASA)
    assert(res("angleC") == 45.0)
    assert(res("sideA") == 464.45)
    assert(res("sideB") == 416.41)
  }

  test("assert SAA") {
    val mapSAA = Map("angleA" -> Some(25.0), "angleB" -> Some(20.0), "sideA" -> Some(80.4))
    val res = trigonometryService.calculate(mapSAA)
    assert(res("angleC") == 135.0)
    assert(res("sideC") == 134.52)
    assert(res("sideB") == 65.07)
    assert(res("sideC") == 134.52)
  }

  test("assert SSA - one solution") {
    val mapSSA = Map("angleA" -> Some(45.0), "sideB" -> Some(7.0), "sideA" -> Some(10.0))
    val res = trigonometryService.calculate(mapSSA)
    assert(res("angleB_1") == 29.67)
    assert(res("angleC_1") == 105.33)
    assert(res("sideC_1") == 13.64)
    assert(!res.contains("angleB_2"))
  }

  test("assert SSA - two solutions") {
    val mapSSA = Map("angleA" -> Some(40.0), "sideB" -> Some(250.0), "sideA" -> Some(180.0))
    val res = trigonometryService.calculate(mapSSA)
    assert(res("angleB_1") == 63.22)
    assert(res("angleB_2") == 116.78)
    assert(res("angleC_1") == 76.78)
    assert(res("angleC_2") == 23.22)
    assert(res("sideC_1") == 272.61)
    assert(res("sideC_2") == 110.42)
  }

  test("assert SSA - no solutions") {
    val mapSSA = Map("angleA" -> Some(42.0), "sideB" -> Some(122.0), "sideA" -> Some(70.0))
    val res = trigonometryService.calculate(mapSSA)
    assert(res("error") == 2.0)
  }

  test("assert SSS") {
    val mapSSS = Map("sideA" -> Some(5.0), "sideB" -> Some(8.0), "sideC" -> Some(12.0))
    val res = trigonometryService.calculate(mapSSS)
    assert(res("angleA") == 17.61)
    assert(res("angleB") == 28.96)
    assert(res("angleC") == 133.43)
  }

  test("assert SAS") {
    val mapSAS = Map("angleA" -> Some(46.5), "sideB" -> Some(10.5), "sideC" -> Some(18.0))
    val res = trigonometryService.calculate(mapSAS)
    assert(res("angleB") == 35.26)
    assert(res("angleC") == 98.24)
    assert(res("sideA") == 13.19)
  }

}