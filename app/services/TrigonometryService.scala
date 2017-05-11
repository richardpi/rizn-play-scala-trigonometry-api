package services

object TrigonometryService {
  val errors = Map(
    1 -> "cannot conclude answer using given information",
    2 -> "no triangle satisfies the conditions given",
    3 -> "sum of angleA and angleB must be less than 180",
    4 -> "angleA must be less than 180"
  )
}

class TrigonometryService {

  def calculate(data:Map[String, Option[Double]]):Map[String,Double] = {

    if /*SAS*/ (contains(List("sideB", "sideC", "angleA"), data)) {
      val errors = validateSAS(data)
      if (errors.isDefined) Map("error" -> errors.get) else calculateSAS(data)
    } /*SSS*/ else if (contains(List("sideA", "sideB", "sideC"), data)) {
      val errors = validateSSS(data)
      if (errors.isDefined) Map("error" -> errors.get) else calculateSSS(data)
    } /*SSA*/ else if (contains(List("angleA", "sideA", "sideB"), data)) {
      val errors = validateSSA(data)
      if (errors.isDefined) Map("error" -> errors.get) else calculateSSA(data)
    } /*SAA*/ else if (contains(List("angleA", "sideA", "angleB"), data)) {
      val errors = validateSAA(data)
      if (errors.isDefined) Map("error" -> errors.get) else calculateSAA(data)
    } /*ASA*/ else if (contains(List("angleA", "sideC", "angleB"), data)) {
      val errors = validateASA(data)
      if (errors.isDefined) Map("error" -> errors.get) else calculateASA(data)
    } else {
      Map("error" -> 1)
    }

  }

  //SAS
  def calculateSAS(data:Map[String, Option[Double]]):Map[String, Double] = {
    val angleA  = data("angleA").get
    val sideB  = data("sideB").get
    val sideC  = data("sideC").get

    val sideA = lawOfCosinesSide(sideB, sideC, angleA)

    val cosB = lawOfCosinesSidesCos(sideB, sideA, sideC)
    val cosC = lawOfCosinesSidesCos(sideC, sideA, sideB)

    val angleBdeg = radToDegree(Math.acos(cosB))
    val angleCdeg = radToDegree(Math.acos(cosC))

    Map(
      "sideA" -> round(sideA),
      "sideB" -> sideB,
      "sideC" -> sideC,
      "angleA" -> angleA,
      "angleB" -> round(angleBdeg),
      "angleC" -> round(angleCdeg)
    )
  }

  //SSS
  def calculateSSS(data:Map[String, Option[Double]]):Map[String, Double] = {
    val sideA  = data("sideA").get
    val sideB  = data("sideB").get
    val sideC  = data("sideC").get

    val cosA = lawOfCosinesSidesCos(sideA, sideB, sideC)
    val cosB = lawOfCosinesSidesCos(sideB, sideA, sideC)
    val cosC = lawOfCosinesSidesCos(sideC, sideA, sideB)

    val angleAdeg = radToDegree(Math.acos(cosA))
    val angleBdeg = radToDegree(Math.acos(cosB))
    val angleCdeg = radToDegree(Math.acos(cosC))

    Map(
      "sideA" -> sideA,
      "sideB" -> sideB,
      "sideC" -> sideC,
      "angleA" -> round(angleAdeg),
      "angleB" -> round(angleBdeg),
      "angleC" -> round(angleCdeg)
    )
  }

  //SSA
  def calculateSSA(data:Map[String, Option[Double]]):Map[String, Double] = {
    val angleA = data("angleA").get
    val sideA  = data("sideA").get
    val sideB  = data("sideB").get

    val sinB = lawOfSinesSin(sideA, angleA, sideB)
    val angleB_1 = radToDegree(Math.asin(sinB))
    val angleB_2 = 180 - angleB_1

    val angleC_1 = 180 - angleA - angleB_1
    val angleC_2 = 180 - angleA - angleB_2

    val sideC_1 = lawOfSinesSide(angleB_1, angleC_1, sideB)
    val sideC_2 = lawOfSinesSide(angleB_2, angleC_2, sideB)

    val mapGeneric = Map(
      "angleA" -> round(angleA),
      "sideA" -> round(sideA),
      "sideB" -> round(sideB)
    )

    val mapVer1 = Map(
      "angleB_1" -> round(angleB_1),
      "angleC_1" -> round(angleC_1),
      "sideC_1" -> round(sideC_1)
    )

    val mapVer2 = Map(
      "angleB_2" -> round(angleB_2),
      "angleC_2" -> round(angleC_2),
      "sideC_2" -> round(sideC_2)
    )

    if (angleC_2 <= 0) mapGeneric ++ mapVer1 else mapGeneric ++ mapVer1 ++ mapVer2
  }

  //SAA
  def calculateSAA(data:Map[String, Option[Double]]):Map[String, Double] = {
    val angleA = data("angleA").get
    val angleB = data("angleB").get
    val sideA  = data("sideA").get

    val angleC = 180.0 - angleA - angleB
    val sideB = round(lawOfSinesSide(angleA, angleB, sideA))
    val sideC = round(lawOfSinesSide(angleA, angleC, sideA))

    Map(
      "angleA" -> angleA,
      "angleB" -> angleB,
      "angleC" -> angleC,
      "sideA" -> sideA,
      "sideB" -> sideB,
      "sideC" -> sideC
    )
  }

  //ASA
  def calculateASA(data:Map[String, Option[Double]]):Map[String, Double] = {
    val angleA = data("angleA").get
    val angleB = data("angleB").get
    val sideC  = data("sideC").get

    val angleC = 180.0 - angleA - angleB
    val sideA = round(lawOfSinesSide(angleC, angleA, sideC))
    val sideB = round(lawOfSinesSide(angleC, angleB, sideC))

    Map(
      "angleA" -> angleA,
      "angleB" -> angleB,
      "angleC" -> angleC,
      "sideA" -> sideA,
      "sideB" -> sideB,
      "sideC" -> sideC
    )
  }

  def validateSAS(data:Map[String, Option[Double]]): Option[Double] = {
    val angleA  = data("angleA").get
    val sideB  = data("sideB").get
    val sideC  = data("sideC").get

    val sideA = lawOfCosinesSide(sideB, sideC, angleA)

    val cosB = lawOfCosinesSidesCos(sideB, sideA, sideC)
    val cosC = lawOfCosinesSidesCos(sideC, sideA, sideB)

    if (cosB > 1 || cosB < -1 || cosC > 1 || cosC < -1)
      Some(2)
    else if (angleA >= 180)
      Some(4)
    else
      None
  }

  def validateSSS(data:Map[String, Option[Double]]): Option[Double] = {
    val sideA  = data("sideA").get
    val sideB  = data("sideB").get
    val sideC  = data("sideC").get

    val cosA = lawOfCosinesSidesCos(sideA, sideB, sideC)
    val cosB = lawOfCosinesSidesCos(sideB, sideA, sideC)
    val cosC = lawOfCosinesSidesCos(sideC, sideA, sideB)

    if (cosA > 1 || cosA < -1 || cosB > 1 || cosB < -1 || cosC > 1 || cosC < -1)
      Some(2)
    else
      None
  }

  def validateSSA(data:Map[String, Option[Double]]): Option[Double] = {
    val angleA = data("angleA").get
    val sideA  = data("sideA").get
    val sideB  = data("sideB").get

    val sinB = lawOfSinesSin(sideA, angleA, sideB)

    if (sinB > 1 || sinB < -1)
      Some(2)
    else
      None
  }

  def validateSAA(data:Map[String, Option[Double]]): Option[Double] = {
    val angleA = data("angleA").get
    val angleB = data("angleB").get

    if (angleA + angleB >= 180)
      Some(3)
    else
      None
  }

  def validateASA(data:Map[String, Option[Double]]): Option[Double] = validateSAA(data)

  /////
  def contains(l: List[String], data: Map[String, Option[Double]]) = l.map(data.contains(_)).find(false == _).getOrElse(true)

  def lawOfSinesSide(angleA: Double, angleB:Double, sideA:Double):Double = sideA * Math.sin(degreeToRad(angleB)) / Math.sin(degreeToRad(angleA))
  def lawOfSinesSin(sideA: Double, angleA: Double, sideB: Double) = sideB * Math.sin(degreeToRad(angleA)) / sideA

  def lawOfCosinesSide(sideB:Double, sideC:Double, angleA:Double) = Math.sqrt(Math.pow(sideB,2) + Math.pow(sideC,2) - 2 * sideB * sideC * Math.cos(degreeToRad(angleA)))
  def lawOfCosinesSidesCos(sideA:Double, sideB:Double, sideC:Double):Double = (Math.pow(sideB,2) + Math.pow(sideC,2) - Math.pow(sideA,2)) / (2 * sideB * sideC)

  def degreeToRad(degree:Double): Double = degree * Math.PI/180
  def radToDegree(rad:Double): Double = rad * 180/Math.PI

  def round(number:Double):Double = BigDecimal(number).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

}
