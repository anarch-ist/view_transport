<?php
namespace DAO;

interface IInvoicesForUserEntity
{
    function selectAllData();

    function selectDataByKey($keyword);
}