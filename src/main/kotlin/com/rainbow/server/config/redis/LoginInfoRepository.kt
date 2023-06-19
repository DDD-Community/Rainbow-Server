package com.rainbow.server.config.redis

import org.springframework.data.repository.CrudRepository

interface LoginInfoRepository:CrudRepository<LoginInfo,String> {
}