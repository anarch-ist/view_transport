<?php
/**
 * Автотест помощника для работы с булевыми значениями.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\BoolHelper,
    PHPUnit_Framework_TestCase;

class BoolHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsBool()
    {
        $true  = true;
        $false = false;

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(BoolHelper::isBool($true));
        $this->assertTrue(BoolHelper::isBool($false));

        $this->assertFalse(BoolHelper::isBool($int));
        $this->assertFalse(BoolHelper::isBool($float));
        $this->assertFalse(BoolHelper::isBool($string));
        $this->assertFalse(BoolHelper::isBool($array));
    }

    public function testIsTrue()
    {
        $true  = true;
        $false = false;

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(BoolHelper::isTrue($true));

        $this->assertFalse(BoolHelper::isTrue($false));
        $this->assertFalse(BoolHelper::isTrue($int));
        $this->assertFalse(BoolHelper::isTrue($float));
        $this->assertFalse(BoolHelper::isTrue($string));
        $this->assertFalse(BoolHelper::isTrue($array));
    }

    public function testIsFalse()
    {
        $true  = true;
        $false = false;

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(BoolHelper::isFalse($false));

        $this->assertFalse(BoolHelper::isFalse($true));
        $this->assertFalse(BoolHelper::isFalse($int));
        $this->assertFalse(BoolHelper::isFalse($float));
        $this->assertFalse(BoolHelper::isFalse($string));
        $this->assertFalse(BoolHelper::isFalse($array));
    }

    public function testEnsureBool()
    {
        $bool = true;

        $ensuredBool = &BoolHelper::ensureBool($bool);

        $ensuredBool = false;

        $this->assertEquals(false, $bool);

        BoolHelper::ensureBool($ensuredBool);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeBool = array();

        BoolHelper::ensureBool($mayBeBool);
    }

    public function testEnsureTrue()
    {
        $true = true;

        $ensuredTrue = &BoolHelper::ensureTrue($true);

        $ensuredTrue = false;

        $this->assertEquals(false, $true);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeBool = array();

        BoolHelper::ensureTrue($mayBeBool);
    }

    public function testEnsureFalse()
    {
        $false = false;

        $ensuredFalse = &BoolHelper::ensureFalse($false);

        $ensuredFalse = true;

        $this->assertEquals(true, $false);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeBool = array();

        BoolHelper::ensureFalse($mayBeBool);
    }

}