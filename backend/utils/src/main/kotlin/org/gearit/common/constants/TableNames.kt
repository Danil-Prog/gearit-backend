package org.gearit.common.constants

/**
 * Наименования таблиц БД.
 */
object TableNames {

    const val PREFIX: String = "gearit_"

    const val USER_PROVIDER: String = PREFIX + "user_provider"

    const val ACCOUNT_INFO: String = PREFIX + "account_info"

    const val ACTION_CODES: String = PREFIX + "action_codes"

    const val NOTIFICATIONS: String = PREFIX + "notifications"

    const val ENDPOINTS: String = PREFIX + "endpoints"

    const val ACCESS_POLICIES: String = PREFIX + "access_policies"
    const val ACCESS_POLICIES_ENDPOINTS: String = PREFIX + "access_policies_endpoints"

    const val AUTOMOBILE_MODELS: String = PREFIX + "automobile_models"
    const val AUTOMOBILE_FACTORIES: String = PREFIX + "automobile_factories"
    const val AUTOMOBILE_FACTORIES_MODELS: String = PREFIX + "automobile_factories_models"
    const val AUTOMOBILES: String = PREFIX + "automobiles"

    const val FEEDBACKS: String = PREFIX + "feedbacks"

    const val ORGANIZATIONS: String = PREFIX + "organizations"
    const val ORGANIZATION_REQUESTS: String = PREFIX + "organization_requests"
    const val ORGANIZATION_REQUESTS_FEEDBACKS: String = PREFIX + "organization_requests_feedbacks"
    const val ORGANIZATION_ADDRESSES: String = PREFIX + "organization_addresses"
}