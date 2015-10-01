<?php
/**
 * Автотест помощника для работы с ресурсами.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\ResourceHelper,
    PHPUnit_Framework_TestCase;

class ResourceHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsResource()
    {
        $resource = fopen(__FILE__, 'r');

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(ResourceHelper::isResource($resource));

        $this->assertFalse(ResourceHelper::isResource($int));
        $this->assertFalse(ResourceHelper::isResource($float));
        $this->assertFalse(ResourceHelper::isResource($string));
        $this->assertFalse(ResourceHelper::isResource($array));

        fclose($resource);
    }

    public function testEnsureResource()
    {
        $resource = fopen(__FILE__, 'r');

        $ensuredResource = &ResourceHelper::ensureResource($resource);

        fseek($ensuredResource, 100);

        $this->assertEquals(100, ftell($resource));

        ResourceHelper::ensureResource($ensuredResource);

        fclose($resource);
    }

    public function testEnsureResourceNotResource()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeResource = 100;

        ResourceHelper::ensureResource($mayBeResource);
    }

}