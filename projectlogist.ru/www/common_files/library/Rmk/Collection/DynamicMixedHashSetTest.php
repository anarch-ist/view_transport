<?php
/**
 * Автотест динамической хешированной карты общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\DynamicMixedHashSet,
    stdClass,
    Exception,
    PHPUnit_Framework_TestCase;

class DynamicMixedHashSetTest extends PHPUnit_Framework_TestCase
{

    /**
     * Тестовые данные.
     *
     * @var array
     */
    protected $array;

    /**
     * Список общего назначения.
     *
     * @var DynamicMixedHashSet
     */
    protected $set;

    /**
     * Функция хеширования.
     * 
     * @var Closure
     */
    protected $keyFunction;

    public function setUp()
    {
        $this->keyFunction = function($value) {
                    return (string) $value;
                };

        $this->set = new DynamicMixedHashSet($this->keyFunction);

        $this->array = array(
            'v1' => 'v1',
            'v2' => 'v2',
            'v3' => 'v3'
        );

        $this->set->fromArray($this->array);
    }

    public function tearDown()
    {
        $this->set->clear();
    }

    public function testConstruct()
    {
        $newSet = new DynamicMixedHashSet($this->keyFunction);

        $this->assertEquals($this->keyFunction, $this->set->getKeyFunction());
        $this->assertEmpty($newSet->toArray());

        $newSetFromArray = new DynamicMixedHashSet(
                        $this->keyFunction, $this->array
        );

        $this->assertEquals($this->array, $newSetFromArray->toArray());
    }

}